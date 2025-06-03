package br.com.vrsoftware.service.plotting.coverage;

import br.com.vrsoftware.dto.plotting.CoverageDataDTO;
import br.com.vrsoftware.exceptions.PlottingException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class CoverageChartService {

    private static final double DEFAULT_ALPHA_EMA = 0.8;

    private static final Color EXECUTED_COLOR = new Color(0, 102, 204); // Dark Blue
    private static final Color FORECASTED_COLOR = new Color(255, 204, 0); // Yellow
    private static final Color ESTIMATED_COLOR = new Color(0, 204, 204); // Cyan

    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 400;

    private final CoverageStatisticsService coverageStatisticsService = new CoverageStatisticsService();

    public CoverageStatisticsService getCoverageStatisticsService() {
        return this.coverageStatisticsService;
    }

    public String generateCoverageChartAsBase64(List<CoverageDataDTO> coverageData) {
        JFreeChart chart = generateChart(coverageData);
        // Convert chart to Base64 image string
        return chartToBase64Image(chart);
    }

    public byte[] generateCoverageChartAsImage(List<CoverageDataDTO> coverageData) {
        JFreeChart chart = generateChart(coverageData);
        // Convert chart to Base64 image string
        return chartToBytes(chart);
    }

    private JFreeChart generateChart(List<CoverageDataDTO> coverageData) {
        coverageStatisticsService.calculateEMAForecast(coverageData, DEFAULT_ALPHA_EMA);

        // Create dataset
        DefaultCategoryDataset dataset = createDataset(coverageData);

        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Code Coverage by Month",       // chart title
                "Month",                        // domain axis label
                "Coverage Percentage",          // range axis label
                dataset,                        // data
                PlotOrientation.VERTICAL,       // orientation
                true,                           // include legend
                true,                           // tooltips
                false                           // URLs
        );

        customizeChart(chart, coverageData);

        return chart;
    }

    /**
     * Create a dataset from the coverage data
     */
    private DefaultCategoryDataset createDataset(List<CoverageDataDTO> coverageData) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (CoverageDataDTO data : coverageData) {
            String month = data.getMonth();

            // Add executed percentage (only if > 0)
            if (data.getExecutedPercentage() >= 0) {
                dataset.addValue(data.getExecutedPercentage(), "Executed", month);
            }

            // Add estimated percentage (only if > 0)
            if (data.getEstimatedPercentage() >= 0) {
                dataset.addValue(data.getEstimatedPercentage(), "Estimated", month);
            }

            // Add forecasted percentage (only if executed is 0 and forecasted > 0)
            if (data.getExecutedPercentage() == 0 && data.getForecastedPercentage() > 0) {
                dataset.addValue(data.getForecastedPercentage(), "Forecasted", month);
            }
        }

        return dataset;
    }

    /**
     * Customize the appearance of the chart
     */
    private void customizeChart(JFreeChart chart, List<CoverageDataDTO> data) {
        // Get the plot and renderer
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // Set custom colors with transparency for forecasted data
        renderer.setSeriesPaint(0, EXECUTED_COLOR);      // Executed series
        renderer.setSeriesPaint(1, ESTIMATED_COLOR);     // Estimated series
        renderer.setSeriesPaint(2, FORECASTED_COLOR);    // Forecasted series

        // Customize the appearance
        renderer.setDrawBarOutline(true);
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());

        // Customize domain axis (months)
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.2);
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);

        // Customize range axis (percentages)
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setNumberFormatOverride(new java.text.DecimalFormat("0.00'%'"));

        // Determine a good minimum/maximum for y-axis based on data
        double minValue = data.stream()
                .mapToDouble(d -> Math.min(
                        d.getExecutedPercentage() > 0 ? d.getExecutedPercentage() : 100,
                        Math.min(
                                d.getEstimatedPercentage() > 0 ? d.getEstimatedPercentage() : 100,
                                d.getForecastedPercentage() > 0 ? d.getForecastedPercentage() : 100)
                ))
                .min()
                .orElse(0);

        double maxValue = 100;
//                data.stream()
//                .mapToDouble(d -> Math.max(
//                        d.getExecutedPercentage(),
//                        Math.max(d.getEstimatedPercentage(), d.getForecastedPercentage())))
//                .max()
//                .orElse(100);

        // Set range with padding
        rangeAxis.setRange(Math.max(0, minValue * 0.9), maxValue);

        // Set chart background
        chart.setBackgroundPaint(Color.WHITE);
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // Set a custom legend
        chart.getLegend().setFrame(org.jfree.chart.block.BlockBorder.NONE);
    }

    /**
     * Convert JFreeChart to Base64 encoded image string
     */
    private String chartToBase64Image(JFreeChart chart) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(outputStream, chart, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            throw new PlottingException("Failed to generate chart image", e);
        }
    }

    /**
     * Generate a worklog pie chart as raw bytes (for direct responses)
     */
    private byte[] chartToBytes(JFreeChart chart) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(outputStream, chart, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new PlottingException("Failed to generate chart image", e);
        }
    }
}

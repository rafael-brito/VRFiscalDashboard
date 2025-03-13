package br.com.vrsoftware.service.plotting;

import br.com.vrsoftware.dto.jira.ActorDTO;
import br.com.vrsoftware.dto.jira.WorklogDTO;
import br.com.vrsoftware.dto.jira.issue.IssueDTO;
import br.com.vrsoftware.exceptions.PlottingException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PieChartService {

    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 400;

    /**
     * Generate a worklog pie chart as a Base64-encoded image string for web display
     */
    public String generateWorklogChartAsBase64(IssueDTO issue) {
        // Check if worklog data exists
        if (issue.getFields() == null ||
                issue.getFields().getWorklog() == null ||
                issue.getFields().getWorklog().getWorklogs() == null ||
                issue.getFields().getWorklog().getWorklogs().isEmpty()) {
            return null; // No data to display
        }

        try {
            // Create the dataset
            DefaultPieDataset dataset = prepareDataset(groupWorklogs(issue.getFields().getWorklog()));

            // Create the chart
            JFreeChart chart = prepareChart(dataset, "Worklog Distribution for " + issue.getKey());

            // Convert chart to PNG byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ChartUtils.writeChartAsPNG(outputStream, chart, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            byte[] chartBytes = outputStream.toByteArray();

            // Convert to Base64 for embedding in HTML
            return Base64.getEncoder().encodeToString(chartBytes);

        } catch (IOException e) {
            throw new PlottingException("Failed to generate chart image", e);
        }
    }

    /**
     * Generate a worklog pie chart as raw bytes (for direct responses)
     */
    public byte[] generateWorklogChartAsBytes(IssueDTO issue) {
        // Check if worklog data exists
        if (issue.getFields() == null ||
                issue.getFields().getWorklog() == null ||
                issue.getFields().getWorklog().getWorklogs() == null ||
                issue.getFields().getWorklog().getWorklogs().isEmpty()) {
            // Return a simple "No data" image
            try {
                DefaultPieDataset emptyDataset = new DefaultPieDataset();
                emptyDataset.setValue("No Data", 1);
                JFreeChart emptyChart = prepareChart(emptyDataset, "No Worklog Data for " + issue.getKey());

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ChartUtils.writeChartAsPNG(outputStream, emptyChart, DEFAULT_WIDTH, DEFAULT_HEIGHT);
                return outputStream.toByteArray();
            } catch (IOException e) {
                throw new PlottingException("Failed to generate empty chart", e);
            }
        }

        try {
            // Create the dataset
            DefaultPieDataset dataset = prepareDataset(groupWorklogs(issue.getFields().getWorklog()));

            // Create the chart
            JFreeChart chart = prepareChart(dataset, "Worklog Distribution for " + issue.getKey());

            // Convert chart to PNG byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ChartUtils.writeChartAsPNG(outputStream, chart, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new PlottingException("Failed to generate chart image", e);
        }
    }

    private DefaultPieDataset prepareDataset(Map<ActorDTO, Double> groupedWorklogs) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<ActorDTO, Double> entry : groupedWorklogs.entrySet()) {
            // Convert seconds to hours for better readability
            double hours = entry.getValue() / 3600.0;
            dataset.setValue(entry.getKey().getDisplayName() + " (" +
                    String.format("%.2f", hours) + "h)", hours);
        }

        return dataset;
    }

    private Map<ActorDTO, Double> groupWorklogs(IssueDTO.JiraFields.Worklog issueWorklog) {
        return issueWorklog.getWorklogs()
                .stream()
                .collect(Collectors.groupingBy(WorklogDTO::getAuthor,
                        Collectors.summingDouble(WorklogDTO::getTimeSpentSeconds)));
    }

    private JFreeChart prepareChart(DefaultPieDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createPieChart(
                title,         // Chart title
                dataset,       // Dataset
                true,         // Include legend
                true,         // Tooltips
                false         // URLs
        );

        // Customize the chart
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 16));

        // Customize the plot
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 12));
        plot.setLabelGap(0.02);
        plot.setNoDataMessage("No worklog data available");
        plot.setShadowGenerator(null); // Disable shadow for cleaner look
        plot.setOutlineVisible(false); // Hide outline for cleaner look
        plot.setBackgroundPaint(Color.WHITE);

        return chart;
    }
}

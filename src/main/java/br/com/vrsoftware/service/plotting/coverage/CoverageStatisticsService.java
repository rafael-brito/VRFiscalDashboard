package br.com.vrsoftware.service.plotting.coverage;

import br.com.vrsoftware.dto.plotting.CoverageDataDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.DoubleBinaryOperator;

@Service
public class CoverageStatisticsService {

    /**
     * Calculate EMA forecasts only for future months (where executedPercentage is 0)
     * Uses cumulative forecasting for future months
     *
     * @param data The list of coverage data
     * @param alpha The EMA smoothing factor (between 0 and 1)
     */
    public void calculateEMAForecast(List<CoverageDataDTO> data, double alpha) {
        if (data == null || data.isEmpty()) {
            return;
        }

        // Find the last month with actual execution data
        int lastExecutedIndex = -1;
        for (int i = data.size() - 1; i >= 0; i--) {
            if (data.get(i).getExecutedPercentage() > 0) {
                lastExecutedIndex = i;
                break;
            }
        }

        // If no execution data, cannot forecast
        if (lastExecutedIndex == -1) {
            return;
        }

        // Keep historical data as-is for months with actual execution
        for (int i = 0; i <= lastExecutedIndex; i++) {
            data.get(i).setForecastedPercentage(data.get(i).getExecutedPercentage());
        }

        // Calculate average growth rate from historical data
        double averageGrowthRate = calculateAverageGrowthRate(data, lastExecutedIndex);

        // Get the last executed value as our starting point
        double lastValue = data.get(lastExecutedIndex).getExecutedPercentage();

        // Now forecast future months with growth trend
        for (int i = lastExecutedIndex + 1; i < data.size(); i++) {
            double forecastValue;

            if (data.get(i).getEstimatedPercentage() > 0) {
                // If we have an estimated value, blend it with our trend projection
                double estimatedValue = data.get(i).getEstimatedPercentage();
                double trendProjection = lastValue * (1 + averageGrowthRate);

                // Blend the estimate with the trend (alpha controls the blend)
                forecastValue = (estimatedValue * alpha) + (trendProjection * (1 - alpha));
            } else {
                // Apply growth trend to previous forecast
                forecastValue = lastValue * (1 + averageGrowthRate);
            }

            // Ensure we don't go backward in months with no estimates
            if (forecastValue < lastValue) {
                forecastValue = lastValue + (lastValue * averageGrowthRate * 0.5);
            }

            // Update the DTO
            data.get(i).setForecastedPercentage(forecastValue);

            // Set this as the new lastValue for next forecast
            lastValue = forecastValue;
        }
    }

    /**
     * Calculate the average month-to-month growth rate from historical data
     *
     * @param data List of coverage data
     * @param lastIndex Index of the last actual data point
     * @return Average growth rate as a decimal (e.g., 0.02 for 2% growth)
     */
    private double calculateAverageGrowthRate(List<CoverageDataDTO> data, int lastIndex) {
        if (lastIndex < 1) {
            return 0.01; // Default to 1% growth if insufficient data
        }

        double sumGrowthRates = 0.0;
        int countPeriods = 0;

        for (int i = 1; i <= lastIndex; i++) {
            double previous = data.get(i-1).getExecutedPercentage();
            double current = data.get(i).getExecutedPercentage();

            if (previous > 0) {
                double growthRate = (current - previous) / previous;
                sumGrowthRates += growthRate;
                countPeriods++;
            }
        }

        // If we couldn't calculate any growth rates, use a small positive default
        if (countPeriods == 0) {
            return 0.01; // Default to 1% growth
        }

        double averageGrowthRate = sumGrowthRates / countPeriods;

        // Ensure a minimum positive growth unless historical data shows decline
        if (averageGrowthRate < 0.001 && sumGrowthRates >= 0) {
            return 0.001; // Minimum 0.1% growth
        }

        return averageGrowthRate;
    }
}

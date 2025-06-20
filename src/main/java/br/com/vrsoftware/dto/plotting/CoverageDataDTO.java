package br.com.vrsoftware.dto.plotting;

import br.com.vrsoftware.config.ObjectMapperConfig;

import java.util.Objects;

public class CoverageDataDTO {
    private final String month;
    private final double estimatedPercentage;
    private final double executedPercentage;
    private double forecastedPercentage;

    public CoverageDataDTO(String month, double estimatedPercentage, double executedPercentage) {
        this.month = month;
        this.estimatedPercentage = estimatedPercentage;
        this.executedPercentage = executedPercentage;
    }

    public String getMonth() {
        return month;
    }

    public double getEstimatedPercentage() {
        return estimatedPercentage;
    }

    public double getExecutedPercentage() {
        return executedPercentage;
    }

    public double getForecastedPercentage() {
        return forecastedPercentage;
    }

    public void setForecastedPercentage(double forecastedPercentage) {
        this.forecastedPercentage = forecastedPercentage;
    }

    @Override
    public String toString() {
        return ObjectMapperConfig.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CoverageDataDTO that = (CoverageDataDTO) o;
        return Double.compare(estimatedPercentage, that.estimatedPercentage) == 0
                && Double.compare(executedPercentage, that.executedPercentage) == 0
                && Double.compare(forecastedPercentage, that.forecastedPercentage) == 0
                && Objects.equals(month, that.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, estimatedPercentage, executedPercentage, forecastedPercentage);
    }
}

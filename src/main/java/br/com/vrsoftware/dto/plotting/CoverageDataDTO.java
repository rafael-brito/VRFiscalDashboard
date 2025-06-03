package br.com.vrsoftware.dto.plotting;

import br.com.vrsoftware.config.ObjectMapperConfig;

import java.util.Objects;

public class CoverageDataDTO {
    private String month;
    private double estimatedPercentage;
    private double executedPercentage;
    private double forecastedPercentage;

    public CoverageDataDTO(String month, double estimatedPercentage, double executedPercentage) {
        this.month = month;
        this.estimatedPercentage = estimatedPercentage;
        this.executedPercentage = executedPercentage;
    }

    public String getMonth() {
        return month;
    }

    public CoverageDataDTO setMonth(String month) {
        this.month = month;
        return this;
    }

    public double getEstimatedPercentage() {
        return estimatedPercentage;
    }

    public CoverageDataDTO setEstimatedPercentage(double estimatedPercentage) {
        this.estimatedPercentage = estimatedPercentage;
        return this;
    }

    public double getExecutedPercentage() {
        return executedPercentage;
    }

    public CoverageDataDTO setExecutedPercentage(double executedPercentage) {
        this.executedPercentage = executedPercentage;
        return this;
    }

    public double getForecastedPercentage() {
        return forecastedPercentage;
    }

    public CoverageDataDTO setForecastedPercentage(double forecastedPercentage) {
        this.forecastedPercentage = forecastedPercentage;
        return this;
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

package br.com.vrsoftware.enums;

public enum MapName {

    CREDENTIALS("credentials"),
    COVERAGE_REPORT_ESTIMATED_PERCENTAGE("coverageReportEstimatedPercentage"),
    COVERAGE_REPORT_EXECUTED_PERCENTAGE("coverageReportExecutedPercentage");

    private final String name;

    MapName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

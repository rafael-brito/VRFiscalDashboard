package br.com.vrsoftware.dto;

import br.com.vrsoftware.config.ObjectMapperConfig;

import java.util.Objects;

public class CustomValuesDTO {

    private double issueTotalHours;
    private String worklogCommentText;
    private String worklogFormattedStartDate;
    private double issueDevelopmentCost;
    private double issueHourlyRate;
    private double worklogTimeSpentHours;

    public CustomValuesDTO() { }

    public double getIssueTotalHours() {
        return issueTotalHours;
    }

    public void setIssueTotalHours(double issueTotalHours) {
        this.issueTotalHours = issueTotalHours;
    }

    public String getWorklogCommentText() {
        return worklogCommentText;
    }

    public void setWorklogCommentText(String worklogCommentText) {
        this.worklogCommentText = worklogCommentText;
    }

    public String getWorklogFormattedStartDate() {
        return worklogFormattedStartDate;
    }

    public void setWorklogFormattedStartDate(String worklogFormattedStartDate) {
        this.worklogFormattedStartDate = worklogFormattedStartDate;
    }

    public double getIssueDevelopmentCost() {
        return issueDevelopmentCost;
    }

    public void setIssueDevelopmentCost(double issueDevelopmentCost) {
        this.issueDevelopmentCost = issueDevelopmentCost;
    }

    public double getIssueHourlyRate() {
        return issueHourlyRate;
    }

    public void setIssueHourlyRate(double issueHourlyRate) {
        this.issueHourlyRate = issueHourlyRate;
    }

    public double getWorklogTimeSpentHours() {
        return worklogTimeSpentHours;
    }

    public void setWorklogTimeSpentHours(double worklogTimeSpentHours) {
        this.worklogTimeSpentHours = worklogTimeSpentHours;
    }

    @Override
    public String toString() {
        return ObjectMapperConfig.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CustomValuesDTO that = (CustomValuesDTO) o;
        return Double.compare(issueTotalHours, that.issueTotalHours) == 0
                && Objects.equals(worklogCommentText, that.worklogCommentText)
                && Objects.equals(worklogFormattedStartDate, that.worklogFormattedStartDate)
                && Double.compare(issueDevelopmentCost, that.issueDevelopmentCost) == 0
                && Double.compare(issueHourlyRate, that.issueHourlyRate) == 0
                && Double.compare(worklogTimeSpentHours, that.worklogTimeSpentHours) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueTotalHours, worklogCommentText, worklogFormattedStartDate,
                issueDevelopmentCost, issueHourlyRate, worklogTimeSpentHours);
    }
}

package br.com.vrsoftware.dto.jira;

import br.com.vrsoftware.config.ObjectMapperConfig;

import java.util.List;
import java.util.Objects;

public class WorklogsDTO {

    Integer startAt;
    Integer maxResults;
    Integer total;
    List<WorklogDTO> worklogs;

    public WorklogsDTO() {}

    public Integer getStartAt() {
        return startAt;
    }

    public void setStartAt(Integer startAt) {
        this.startAt = startAt;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<WorklogDTO> getWorklogs() {
        return worklogs;
    }

    public void setWorklogs(List<WorklogDTO> worklogs) {
        this.worklogs = worklogs;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WorklogsDTO that = (WorklogsDTO) o;
        return Objects.equals(startAt, that.startAt)
                && Objects.equals(maxResults, that.maxResults)
                && Objects.equals(total, that.total)
                && Objects.equals(worklogs, that.worklogs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startAt, maxResults, total, worklogs);
    }

    @Override
    public String toString() {
        return ObjectMapperConfig.toJson(this);
    }
}

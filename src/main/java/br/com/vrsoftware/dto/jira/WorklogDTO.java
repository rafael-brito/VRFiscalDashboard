package br.com.vrsoftware.dto.jira;

import br.com.vrsoftware.config.ObjectMapperConfig;
import br.com.vrsoftware.dto.CustomValuesDTO;

import java.util.List;
import java.util.Objects;

public class WorklogDTO {

    String id;
    String issueId;
    int timeSpentSeconds;
    ActorDTO author;
    Comment comment;
    String started;
    CustomValuesDTO customValues = new CustomValuesDTO();

    public WorklogDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public int getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public void setTimeSpentSeconds(int timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public ActorDTO getAuthor() {
        return author;
    }

    public void setAuthor(ActorDTO author) {
        this.author = author;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public CustomValuesDTO getCustomValues() {
        return customValues;
    }

    public void setCustomValues(CustomValuesDTO customValues) {
        this.customValues = customValues;
    }

    @Override
    public String toString() {
        return ObjectMapperConfig.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WorklogDTO that = (WorklogDTO) o;
        return timeSpentSeconds == that.timeSpentSeconds
                && Objects.equals(id, that.id)
                && Objects.equals(issueId, that.issueId)
                && Objects.equals(author, that.author)
                && Objects.equals(comment, that.comment)
                && Objects.equals(started, that.started)
                && Objects.equals(customValues, that.customValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, issueId, timeSpentSeconds, author, comment, started, customValues);
    }

    public static class Comment {
        List<Content> content;

        public Comment() {
        }

        public Comment(List<Content> content) {
            this.content = content;
        }

        public List<Content> getContent() {
            return content;
        }

        public void setContent(List<Content> content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return ObjectMapperConfig.toJson(this);
        }
    }

    public static class Content {
        List<Content> content;
        String text;

        public Content() {
        }

        public Content(List<Content> content, String text) {
            this.content = content;
            this.text = text;
        }

        public List<Content> getContent() {
            return content;
        }

        public void setContent(List<Content> content) {
            this.content = content;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return ObjectMapperConfig.toJson(this);
        }
    }

}



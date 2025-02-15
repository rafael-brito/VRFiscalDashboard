package br.com.vrsoftware.dto.jira;

import br.com.vrsoftware.config.ObjectMapperConfig;

import java.util.List;

public class WorklogDTO {

    String id;
    String issueId;
    int timeSpentSeconds;
    ActorDTO author;
    Comment comment;

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

    @Override
    public String toString() {
        return ObjectMapperConfig.toJson(this);
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



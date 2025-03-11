package br.com.vrsoftware.entities.jira.issue;

import br.com.vrsoftware.entities.jira.Actor;
import br.com.vrsoftware.entities.jira.Project;
import br.com.vrsoftware.usecases.ObjectMapperConfig;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Issue {

    String id;
    String key;
    JiraFields fields;

    public Issue() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JiraFields getFields() {
        return fields;
    }

    public void setFields(JiraFields fields) {
        this.fields = fields;
    }

    public static class JiraFields {

        @JsonAlias("summary")
        @JsonProperty("title")
        String title;
        Actor creator;
        Actor reporter;
        List<VRComponent> components;
        Status status;
        @JsonAlias("customfield_10040")
        @JsonProperty("version")
        Version version;
        Worklog worklog;
        Project project;

        public JiraFields() {}

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Actor getCreator() {
            return creator;
        }

        public void setCreator(Actor creator) {
            this.creator = creator;
        }

        public Actor getReporter() {
            return reporter;
        }

        public void setReporter(Actor reporter) {
            this.reporter = reporter;
        }

        public List<VRComponent> getComponents() {
            return components;
        }

        public void setComponents(List<VRComponent> components) {
            this.components = components;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Version getVersion() {
            return version;
        }

        public void setVersion(Version version) {
            this.version = version;
        }

        public Worklog getWorklog() {
            return worklog;
        }

        public void setWorklog(Worklog worklog) {
            this.worklog = worklog;
        }

        public Project getProject() {
            return project;
        }

        public void setProject(Project project) {
            this.project = project;
        }

        @Override
        public String toString() {
            return ObjectMapperConfig.toJson(this);
        }

        public static class Worklog {

            List<Worklog> worklogs;

            public Worklog() {}

            public List<Worklog> getWorklogs() {
                return worklogs;
            }

            public void setWorklogs(List<Worklog> worklogs) {
                this.worklogs = worklogs;
            }

            @Override
            public String toString() {
                return ObjectMapperConfig.toJson(this);
            }
        }
    }

    @Override
    public String toString() {
        return ObjectMapperConfig.toJson(this);
    }
}

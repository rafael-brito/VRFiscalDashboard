package br.com.vrsoftware.dto.jira.issue;

import br.com.vrsoftware.config.ObjectMapperConfig;
import br.com.vrsoftware.dto.jira.ActorDTO;
import br.com.vrsoftware.dto.jira.ProjectDTO;
import br.com.vrsoftware.dto.jira.WorklogDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IssueDTO {

    String id;
    String key;
    JiraFields fields;

    public IssueDTO() { }

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
        ActorDTO creator;
        ActorDTO reporter;
        List<VRComponentDTO> components;
        StatusDTO status;
        @JsonAlias("customfield_10040")
        @JsonProperty("version")
        VersionDTO version;
        Worklog worklog;
        ProjectDTO project;

        public JiraFields() {}

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ActorDTO getCreator() {
            return creator;
        }

        public void setCreator(ActorDTO creator) {
            this.creator = creator;
        }

        public ActorDTO getReporter() {
            return reporter;
        }

        public void setReporter(ActorDTO reporter) {
            this.reporter = reporter;
        }

        public List<VRComponentDTO> getComponents() {
            return components;
        }

        public void setComponents(List<VRComponentDTO> components) {
            this.components = components;
        }

        public StatusDTO getStatus() {
            return status;
        }

        public void setStatus(StatusDTO status) {
            this.status = status;
        }

        public VersionDTO getVersion() {
            return version;
        }

        public void setVersion(VersionDTO version) {
            this.version = version;
        }

        public Worklog getWorklog() {
            return worklog;
        }

        public void setWorklog(Worklog worklog) {
            this.worklog = worklog;
        }

        public ProjectDTO getProject() {
            return project;
        }

        public void setProject(ProjectDTO project) {
            this.project = project;
        }

        @Override
        public String toString() {
            return ObjectMapperConfig.toJson(this);
        }

        public static class Worklog {

            List<WorklogDTO> worklogs;

            public Worklog() {}

            public List<WorklogDTO> getWorklogs() {
                return worklogs;
            }

            public void setWorklogs(List<WorklogDTO> worklogs) {
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

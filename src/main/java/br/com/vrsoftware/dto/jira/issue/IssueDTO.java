package br.com.vrsoftware.dto.jira.issue;

import br.com.vrsoftware.config.ObjectMapperConfig;
import br.com.vrsoftware.dto.CustomValuesDTO;
import br.com.vrsoftware.dto.jira.ActorDTO;
import br.com.vrsoftware.dto.jira.ProjectDTO;
import br.com.vrsoftware.dto.jira.WorklogDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class IssueDTO implements Serializable {

    String id;
    String key;
    JiraFields fields;
    CustomValuesDTO customValues = new CustomValuesDTO();

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

    public CustomValuesDTO getCustomValues() {
        return customValues;
    }

    public void setCustomValues(CustomValuesDTO customValues) {
        this.customValues = customValues;
    }

    public static class JiraFields implements Serializable {

        @JsonAlias("summary")
        @JsonProperty("title")
        String title;
        ActorDTO creator;
        ActorDTO reporter;
        ActorDTO assignee;
        List<VRComponentDTO> components;
        StatusDTO status;
        @JsonAlias("customfield_10040")
        @JsonProperty("version")
        VersionDTO version;
        Worklog worklog;
        ProjectDTO project;
        PriorityDTO priority;
        @JsonAlias("customfield_10033")
        @JsonProperty("developer")
        ActorDTO developer;
        @JsonAlias("customfield_10118")
        @JsonProperty("tester")
        ActorDTO tester;
        @JsonAlias("customfield_10026")
        @JsonProperty("storyPoints")
        String storyPoints;

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

        public ActorDTO getAssignee() {
            return assignee;
        }

        public void setAssignee(ActorDTO assignee) {
            this.assignee = assignee;
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

        public PriorityDTO getPriority() {
            return priority;
        }

        public void setPriority(PriorityDTO priority) {
            this.priority = priority;
        }

        public ActorDTO getDeveloper() {
            return developer;
        }

        public void setDeveloper(ActorDTO developer) {
            this.developer = developer;
        }

        public ActorDTO getTester() {
            return tester;
        }

        public void setTester(ActorDTO tester) {
            this.tester = tester;
        }

        public String getStoryPoints() {
            return storyPoints;
        }

        public void setStoryPoints(String storyPoints) {
            this.storyPoints = storyPoints;
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

package br.com.vrsoftware.dto.jira;

import br.com.vrsoftware.config.ObjectMapperConfig;

public class ProjectDTO {

    private String id;
    private String key;
    private String name;

    public ProjectDTO() {}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ObjectMapperConfig.toJson(this);
    }
}

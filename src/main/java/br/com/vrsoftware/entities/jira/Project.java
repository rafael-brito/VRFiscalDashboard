package br.com.vrsoftware.entities.jira;

import br.com.vrsoftware.usecases.ObjectMapperConfig;

public class Project {

    private String id;
    private String key;
    private String name;

    public Project() {}

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

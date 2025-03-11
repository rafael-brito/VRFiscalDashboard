package br.com.vrsoftware.entities.jira.issue;

import br.com.vrsoftware.usecases.ObjectMapperConfig;

public class Version {

    String id;
    String value;

    public Version() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return ObjectMapperConfig.toJson(this);
    }
}

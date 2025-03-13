package br.com.vrsoftware.dto.jira.issue;

import br.com.vrsoftware.config.ObjectMapperConfig;

public class VersionDTO {

    String id;
    String value;

    public VersionDTO() {}

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

package br.com.vrsoftware.dto.jira.issue;

import br.com.vrsoftware.config.ObjectMapperConfig;

public class VRComponentDTO {

    String id;
    String name;

    public VRComponentDTO() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

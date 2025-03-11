package br.com.vrsoftware.entities.jira.issue;

import br.com.vrsoftware.usecases.ObjectMapperConfig;

public class VRComponent {

    String id;
    String name;

    public VRComponent() { }

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

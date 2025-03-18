package br.com.vrsoftware.dto.jira.issue;

public class PriorityDTO {

    String id;
    String name;

    public PriorityDTO() { }

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
}

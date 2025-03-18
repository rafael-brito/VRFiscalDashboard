package br.com.vrsoftware.enums;

public enum MapName {

    CREDENTIALS("credentials");

    private final String name;

    MapName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

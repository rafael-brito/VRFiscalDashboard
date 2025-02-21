package br.com.vrsoftware.dto.template;

public record WelcomeDTO (String title,
                          String message,
                          String currentUser,
                          String currentDateTime) { }

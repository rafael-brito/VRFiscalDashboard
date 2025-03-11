package infrastructure.repositories;

import application.usecases.interfaces.JiraIssueRepository;
import domain.entities.JiraIssue;
import java.util.List;
import java.util.Arrays;

public class JiraIssueRepositoryImpl implements JiraIssueRepository {
    @Override
    public JiraIssue getById(String id) {
        // Simulando requisição ao Jira (normalmente aqui usaria um cliente HTTP)
        return new JiraIssue(id, "Bug no sistema", "Corrigir erro 500", "TO DO");
    }
}

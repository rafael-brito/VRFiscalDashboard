package application.usecases;

import application.usecases.interfaces.JiraIssueRepository;
import domain.entities.JiraIssue;

public class GetJiraIssueUseCase {
    private final JiraIssueRepository repository;

    public GetJiraIssueUseCase(JiraIssueRepository repository) {
        this.repository = repository;
    }

    public JiraIssue execute(String id) {
        return repository.getById(id);
    }
}

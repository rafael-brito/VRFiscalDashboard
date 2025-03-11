package application.usecases.interfaces;

import domain.entities.JiraIssue;
import java.util.List;

public interface IJiraIssueRepository {
    JiraIssue getById(String id);
}

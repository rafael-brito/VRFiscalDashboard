package br.com.vrsoftware.service.jira;

import br.com.vrsoftware.dto.AuthCredentialsDTO;
import br.com.vrsoftware.dto.jira.issue.IssueDTO;
import br.com.vrsoftware.dto.jira.ProjectDTO;
import br.com.vrsoftware.dto.jira.WorklogDTO;
import br.com.vrsoftware.exceptions.RequestException;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DashboardApiClientService extends BaseApiClientService {

    public DashboardApiClientService(AuthCredentialsDTO authCredentials) {
        super("https://vrsoft.atlassian.net", authCredentials);
    }

    public IssueDTO getIssue() {
        try {
            String issueId = "FIS-4190";
            HttpRequest request = createRequestBuilder("/rest/api/3/issue/" + issueId)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return handleResponse(response, IssueDTO.class);
        } catch (Exception e) {
            throw new RequestException("Erro ao buscar dados do dashboard", e);
        }
    }

    public WorklogDTO getWorklog() {
        try {
            String issueId = "FIS-4190";
            String worklogId = "75024";
            HttpRequest request = createRequestBuilder("/rest/api/3/issue/" + issueId + "/worklog/" + worklogId)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return handleResponse(response, WorklogDTO.class);
        } catch (Exception e) {
            throw new RequestException("Erro ao buscar dados do dashboard", e);
        }
    }

    public ProjectDTO getProject() {
        try {
            String projectId = "FIS";
            HttpRequest request = createRequestBuilder("/rest/api/3/project/" + projectId)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return handleResponse(response, ProjectDTO.class);
        } catch (Exception e) {
            throw new RequestException("Erro ao buscar dados do dashboard", e);
        }
    }
}

package br.com.vrsoftware.adapters;

import br.com.vrsoftware.entities.AuthCredentials;
import br.com.vrsoftware.entities.AuthProxy;
import br.com.vrsoftware.entities.jira.issue.Issue;
import br.com.vrsoftware.entities.jira.Project;
import br.com.vrsoftware.entities.jira.Worklog;
import br.com.vrsoftware.exceptions.RequestException;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DashboardApiClient extends BaseApiClient {

    private static final String BASE_URL = "https://vrsoft.atlassian.net";

    public DashboardApiClient(AuthCredentials authCredentials, AuthProxy pAuthProxy) {
        super("https://vrsoft.atlassian.net", authCredentials, pAuthProxy);
    }

    public Issue getIssue() {
        try {
            String issueId = "FIS-4190";
            HttpRequest request = createRequestBuilder("/rest/api/3/issue/" + issueId)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return handleResponse(response, Issue.class);
        } catch (Exception e) {
            throw new RequestException("Erro ao buscar dados do dashboard", e);
        }
    }

    public Worklog getWorklog() {
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

            return handleResponse(response, Worklog.class);
        } catch (Exception e) {
            throw new RequestException("Erro ao buscar dados do dashboard", e);
        }
    }

    public Project getProject() {
        try {
            String projectId = "FIS";
            HttpRequest request = createRequestBuilder("/rest/api/3/project/" + projectId)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return handleResponse(response, Project.class);
        } catch (Exception e) {
            throw new RequestException("Erro ao buscar dados do dashboard", e);
        }
    }
}

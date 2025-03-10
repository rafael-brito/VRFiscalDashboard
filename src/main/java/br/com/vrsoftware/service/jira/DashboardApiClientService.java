package br.com.vrsoftware.service.jira;

import br.com.vrsoftware.dto.AuthCredentialsDTO;
import br.com.vrsoftware.dto.jira.ProjectDTO;
import br.com.vrsoftware.dto.jira.WorklogDTO;
import br.com.vrsoftware.dto.jira.issue.IssueDTO;
import br.com.vrsoftware.exceptions.RequestException;
import br.com.vrsoftware.service.JsonPathService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Service
public class DashboardApiClientService extends BaseApiClientService {

    private final HttpSession session;
    private final String basePath = "/rest/api/3";

    private final JsonPathService jsonPathService;

    public DashboardApiClientService(
            @Value("${jira.base.url:https://vrsoft.atlassian.net}") String baseUrl,
            HttpSession session,
            JsonPathService jsonPathService) {
        super(baseUrl);
        this.session = session;
        this.jsonPathService = jsonPathService;
    }

    /**
     * Gets the current user's credentials from the session
     */
    private AuthCredentialsDTO getCredentials() {
        AuthCredentialsDTO credentials = (AuthCredentialsDTO) session.getAttribute("userCredentials");
        if (credentials == null) {
            throw new RequestException("User not authenticated. Please log in first.");
        }
        return credentials;
    }

    public IssueDTO getIssue(String issueId) {
        try {
            String path = basePath + "/issue/" + issueId;
            HttpRequest request = createRequestBuilder(path, getCredentials())
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
            String path = basePath + "/issue/" + issueId + "/worklog/" + worklogId;
            HttpRequest request = createRequestBuilder(path, getCredentials())
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
            String path = basePath + "/project/" + projectId;
            HttpRequest request = createRequestBuilder(path, getCredentials())
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

    public List<String> getIssuesByQuery(String pJql) {
        try {
            String path = basePath + "/search/jql";
            HttpRequest request = createRequestBuilder(path, getCredentials(), Optional.of(pJql))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            String issueIds = handleResponse(response, String.class);
            return jsonPathService.extractIssueIds(issueIds);
        } catch (Exception e) {
            throw new RequestException("Erro ao buscar dados do dashboard", e);
        }
    }
}

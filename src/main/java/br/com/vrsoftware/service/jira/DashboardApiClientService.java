package br.com.vrsoftware.service.jira;

import br.com.vrsoftware.dto.AuthCredentialsDTO;
import br.com.vrsoftware.exceptions.RequestException;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DashboardApiClientService extends BaseApiClientService {

    public DashboardApiClientService(AuthCredentialsDTO authCredentials) {
        super("https://vrsoft.atlassian.net", authCredentials);
    }

    public String getIssue() {
        try {
            String issueId = "FIS-4190";
            HttpRequest request = createRequestBuilder("/rest/api/3/issue/" + issueId)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return handleResponse(response);
//            return handleResponse(response, DashboardData.class);
        } catch (Exception e) {
            throw new RequestException("Erro ao buscar dados do dashboard", e);
        }
    }

    public String getIssueWorklogs() {
        try {
            String issueId = "FIS-4190";
            HttpRequest request = createRequestBuilder("/rest/api/3/issue/" + issueId + "/worklog")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return handleResponse(response);
//            return handleResponse(response, DashboardData.class);
        } catch (Exception e) {
            throw new RequestException("Erro ao buscar dados do dashboard", e);
        }
    }

    public String getWorklog() {
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

            return handleResponse(response);
//            return handleResponse(response, DashboardData.class);
        } catch (Exception e) {
            throw new RequestException("Erro ao buscar dados do dashboard", e);
        }
    }
}

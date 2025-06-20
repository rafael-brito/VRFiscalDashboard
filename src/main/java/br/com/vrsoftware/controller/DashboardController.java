package br.com.vrsoftware.controller;

import br.com.vrsoftware.dto.AuthCredentialsDTO;
import br.com.vrsoftware.dto.jira.issue.IssueDTO;
import br.com.vrsoftware.dto.plotting.CoverageDataDTO;
import br.com.vrsoftware.service.jira.DashboardApiClientService;
import br.com.vrsoftware.service.plotting.coverage.CoverageParserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final DashboardApiClientService jiraService;

    @Autowired
    public DashboardController(DashboardApiClientService jiraService) {
        this.jiraService = jiraService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        // Check if user is logged in
        AuthCredentialsDTO credentials = (AuthCredentialsDTO) session.getAttribute("userCredentials");
        if (credentials == null) {
            return "redirect:/login";
        }

        // Get username from credentials
        model.addAttribute("username", credentials.getEmail());

        // Update coverage data
        List<CoverageDataDTO> coverageData = CoverageParserService.parseCoverageCsv();
        session.setAttribute("coverageData", coverageData);

        return "dashboard";
    }

    @PostMapping("/dashboard/clear-session")
    public String clearSession(HttpSession session) {
        session.removeAttribute("issuesList");
        session.removeAttribute("queryParams");
        return "redirect:/dashboard";
    }

    @GetMapping("/search")
    public String searchIssues(
            @RequestParam(value = "project", required = false) String project,
            @RequestParam(value = "sprintValues", required = false) String sprintValues,
            @RequestParam(value = "query", required = false) String query,
            HttpSession session,
            Model model) {

        // Check if user is logged in
        AuthCredentialsDTO credentials = (AuthCredentialsDTO) session.getAttribute("userCredentials");
        if (credentials == null) {
            return "redirect:/login";
        }

        // Set username in the model
        model.addAttribute("username", credentials.getEmail());

        // Build the JQL query based on the parameters
        StringBuilder jqlBuilder = new StringBuilder();

        // Add project criteria if provided
        if (project != null && !project.trim().isEmpty()) {
            jqlBuilder.append("project = ").append(project);
        }

        // Add sprint criteria if provided
        if (sprintValues != null && !sprintValues.trim().isEmpty()) {
            List<String> sprints = Arrays.asList(sprintValues.split(","));
            if (!sprints.isEmpty()) {
                if (jqlBuilder.length() > 0) {
                    jqlBuilder.append(" AND ");
                }

                if (sprints.size() == 1) {
                    jqlBuilder.append("sprint = \"").append(sprints.get(0)).append("\"");
                } else {
                    jqlBuilder.append("sprint in (");
                    jqlBuilder.append(sprints.stream()
                            .map(s -> "\"" + s + "\"")
                            .collect(Collectors.joining(", ")));
                    jqlBuilder.append(")");
                }
            }
        }

        // Add text search criteria if provided
        if (query != null && !query.trim().isEmpty()) {
            if (jqlBuilder.length() > 0) {
                jqlBuilder.append(" AND ");
            }
            jqlBuilder.append("(summary ~ \"").append(query)
                    .append("\" OR description ~ \"").append(query).append("\")");
        }

        // TODO: Ordering criteria.

        // Execute the search
        String jql = jqlBuilder.toString();
        List<IssueDTO> issues = getIssues(jiraService.getIssuesByQuery(jql));

        // Store in session for reports page to access
        session.setAttribute("issuesList", issues);

        // Add results to the model
        model.addAttribute("issues", issues);

        // Return to the dashboard view
        return "dashboard";
    }

    private List<IssueDTO> getIssues(List<String> issueIds) {
        List<IssueDTO> issues = new ArrayList<>();
        for (String issueId : issueIds) {
            IssueDTO issue = jiraService.getIssue(issueId);
            issue.getFields()
                    .getWorklog()
                    .setWorklogs(
                            jiraService.getIssueWorklogs(
                                    issue.getKey()).getWorklogs());
            issues.add(issue);
        }
        return issues;
    }
}
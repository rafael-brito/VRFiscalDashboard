package br.com.vrsoftware.controller;

import br.com.vrsoftware.dto.ReportTypeDTO;
import br.com.vrsoftware.dto.jira.WorklogDTO;
import br.com.vrsoftware.dto.jira.issue.IssueDTO;
import br.com.vrsoftware.service.plotting.PieChartService;
import br.com.vrsoftware.util.CommentTextExtractor;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private static final double HOURLY_RATE = 25.0;

    private final PieChartService pieChartService;

    @Autowired
    public ReportController(PieChartService pieChartService) {
        this.pieChartService = pieChartService;
    }

    /**
     * Main reports page with collapsible tables for report types
     */
    @GetMapping
    public String showReportsPage(Model model, HttpSession session) {
        // Get issues from session that were loaded in dashboard
        @SuppressWarnings("unchecked")
        List<IssueDTO> issues = (List<IssueDTO>) session.getAttribute("issuesList");

        if (issues == null || issues.isEmpty()) {
            return "redirect:/dashboard?error=No+issues+available+for+reporting";
        }

        // Add report types
        List<ReportTypeDTO> reportTypes = new ArrayList<>();
        reportTypes.add(new ReportTypeDTO("worklog", "Worklog Distribution by Actor",
                "View time spent by each team member on selected issues"));

        model.addAttribute("reportTypes", reportTypes);
        model.addAttribute("issues", issues);

        return "reports";
    }

    /**
     * Generate worklog report for a specific issue
     */
    @GetMapping("/worklog/{issueKey}")
    public String generateWorklogReport(@PathVariable String issueKey, Model model, HttpSession session) {
        List<IssueDTO> issues = (List<IssueDTO>) session.getAttribute("issuesList");
        IssueDTO issue = issues.stream().filter(x -> x.getKey().equals(issueKey)).findAny().orElse(null);

        if (issue == null) {
            return "redirect:/reports?error=Issue+not+found";
        }

        updateWorklogExtractedText(issue);
        updateWorklogStartedTime(issue);
        updateIssueTotalHours(issue);
        updateIssueDevelopmentCost(issue);

        // Generate chart as Base64 for embedding in HTML
        String base64Chart = pieChartService.generateWorklogChartAsBase64(issue);

        model.addAttribute("issue", issue);
        model.addAttribute("chartImage", "data:image/png;base64," + base64Chart);
        model.addAttribute("reportType", "Worklog Distribution");

        return "report-detail";
    }

    /**
     * Endpoint to get the chart as an image (for download)
     */
    @GetMapping("/worklog/{issueKey}/image")
    public ResponseEntity<byte[]> getWorklogChartImage(@PathVariable String issueKey, HttpSession session) {
        List<IssueDTO> issues = (List<IssueDTO>) session.getAttribute("issuesList");
        IssueDTO issue = issues.stream().filter(x -> x.getKey().equals(issueKey)).findAny().orElse(null);

        if (issue == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] chartBytes = pieChartService.generateWorklogChartAsBytes(issue);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("attachment", issueKey + "-worklog.png");

        return new ResponseEntity<>(chartBytes, headers, HttpStatus.OK);
    }

    private void updateWorklogExtractedText(IssueDTO issue) {
        for (WorklogDTO worklog : issue.getFields().getWorklog().getWorklogs()) {
            worklog.getCustomValues().setWorklogCommentText(CommentTextExtractor.extractText(worklog.getComment()));
        }
    }

    private void updateWorklogStartedTime(IssueDTO issue) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("pt", "BR"));
        for (WorklogDTO worklog : issue.getFields().getWorklog().getWorklogs()) {
            OffsetDateTime dateTime = OffsetDateTime.parse(worklog.getStarted(), inputFormatter);
            worklog.getCustomValues().setWorklogFormattedStartDate(dateTime.format(outputFormatter));
        }
    }

    private void updateIssueTotalHours(IssueDTO issue) {
        for (WorklogDTO worklog : issue.getFields().getWorklog().getWorklogs()) {
            worklog.getCustomValues().setWorklogTimeSpentHours(worklog.getTimeSpentSeconds() / 3600.0);
        }
        double totalHours = issue.getFields().getWorklog().getWorklogs().stream()
                .mapToDouble(x -> x.getCustomValues().getWorklogTimeSpentHours()).sum();
        issue.getCustomValues().setIssueTotalHours(totalHours);
    }

    private void updateIssueDevelopmentCost(IssueDTO issue) {
        issue.getCustomValues().setIssueHourlyRate(HOURLY_RATE);
        double totalCost = issue.getCustomValues().getIssueTotalHours() * issue.getCustomValues().getIssueHourlyRate();
        issue.getCustomValues().setIssueDevelopmentCost(totalCost);
    }
}
package br.com.vrsoftware.controller;

import br.com.vrsoftware.dto.ReportTypeDTO;
import br.com.vrsoftware.dto.jira.issue.IssueDTO;
import br.com.vrsoftware.service.jira.SummarizeService;
import br.com.vrsoftware.service.plotting.PieChartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final PieChartService pieChartService;
    private final SummarizeService summarizeService;

    @Autowired
    public ReportController(PieChartService pieChartService,
                            SummarizeService summarizeService) {
        this.pieChartService = pieChartService;
        this.summarizeService = summarizeService;
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

        summarizeService.updateWorklogExtractedText(issue);
        summarizeService.updateWorklogStartedTime(issue);
        summarizeService.updateIssueTotalHours(issue);
        summarizeService.updateIssueDevelopmentCost(issue);

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
}
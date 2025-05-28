package br.com.vrsoftware.controller;

import br.com.vrsoftware.dto.ReportTypeDTO;
import br.com.vrsoftware.dto.jira.issue.IssueDTO;
import br.com.vrsoftware.dto.plotting.CoverageDataDTO;
import br.com.vrsoftware.request.EmailRequest;
import br.com.vrsoftware.service.Email.EmailService;
import br.com.vrsoftware.service.jira.SummarizeService;
import br.com.vrsoftware.service.plotting.WorklogChartService;
import br.com.vrsoftware.service.plotting.coverage.CoverageChartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final WorklogChartService worklogChartService;
    private final SummarizeService summarizeService;
    private final CoverageChartService coverageChartService;
    private final EmailService emailService;

    @Autowired
    public ReportController(WorklogChartService worklogChartService,
                            SummarizeService summarizeService,
                            CoverageChartService coverageChartService,
                            EmailService emailService) {
        this.worklogChartService = worklogChartService;
        this.summarizeService = summarizeService;
        this.coverageChartService = coverageChartService;
        this.emailService = emailService;
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
            issues = new ArrayList<>();
        }

        // Add report types
        List<ReportTypeDTO> reportTypes = new ArrayList<>();
        reportTypes.add(new ReportTypeDTO("worklog", "Worklog Distribution by Actor",
                "View time spent by each team member on selected issues", true));
        reportTypes.add(new ReportTypeDTO("coverage", "Code Coverage by Month",
                "View code coverage statistics for VRCore project", false));

        // Get coverage data and calculate forecast
        @SuppressWarnings("unchecked")
        List<CoverageDataDTO> coverageData = (List<CoverageDataDTO>) session.getAttribute("coverageData");
        if (coverageData != null && !coverageData.isEmpty()) {
            // Calculate forecast when loading the page
            coverageChartService.getCoverageStatisticsService().calculateEMAForecast(coverageData, 0.8);
            // Update the session with the new forecasted data
            session.setAttribute("coverageData", coverageData);
        }

        model.addAttribute("reportTypes", reportTypes);
        model.addAttribute("issues", issues);
        model.addAttribute("coverageData", coverageData);

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
        String base64Chart = worklogChartService.generateWorklogChartAsBase64(issue);

        model.addAttribute("issue", issue);
        model.addAttribute("worklogChartImage", "data:image/png;base64," + base64Chart);
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

        byte[] chartBytes = worklogChartService.generateWorklogChartAsBytes(issue);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("attachment", issueKey + "-worklog.png");

        return new ResponseEntity<>(chartBytes, headers, HttpStatus.OK);
    }


    /**
     * Generate coverage report
     */
    @GetMapping("/coverage")
    public String generateCoverageReport(Model model, HttpSession session) {
        // Generate chart as Base64 for embedding in HTML
        List<CoverageDataDTO> coverageData = (List<CoverageDataDTO>) session.getAttribute("coverageData");

        if (coverageData == null || coverageData.isEmpty()) {
            return "redirect:/reports?error=No+coverage+data+available";
        }

        // Calculate forecast
        coverageChartService.getCoverageStatisticsService().calculateEMAForecast(coverageData, 0.8);

        // Generate chart
        String base64Chart = coverageChartService.generateCoverageChartAsBase64(coverageData);

        // Update session with the new forecasted data
        session.setAttribute("coverageData", coverageData);

        model.addAttribute("coverageChartImage", "data:image/png;base64," + base64Chart);
        model.addAttribute("coverageData", coverageData);
        model.addAttribute("alpha", 0.7);
        model.addAttribute("filename", "coverageData.csv");
        model.addAttribute("reportType", "Code Coverage Analysis");

        return "report-detail";
    }

    /**
     * Endpoint to get the chart as an image (for download)
     */
    @GetMapping("/coverage/image")
    public ResponseEntity<byte[]> getCoverageChartImage(HttpSession session) {
        List<CoverageDataDTO> coverageData = (List<CoverageDataDTO>) session.getAttribute("coverageData");
        byte[] chartBytes = coverageChartService.generateCoverageChartAsImage(coverageData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("attachment", "coverage.png");

        return new ResponseEntity<>(chartBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/send-email-coverage")
    @ResponseBody
    public ResponseEntity<String> sendReportEmailCoverage(@RequestBody EmailRequest emailRequest, HttpSession session) {
        String subject = emailRequest.getSubject();
        String message = emailRequest.getMessage();

        try {
            if (subject != null && subject.contains("Coverage")) {
                @SuppressWarnings("unchecked")
                List<CoverageDataDTO> coverageData = (List<CoverageDataDTO>) session.getAttribute("coverageData");
                if (coverageData == null || coverageData.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum dado de coverage encontrado.");
                }
                byte[] chartBytes = coverageChartService.generateCoverageChartAsImage(coverageData);

                emailService.sendEmailWithAttachment(
                        emailRequest.getTo(),
                        subject,
                        message + "\n\nSegue em anexo o gráfico de cobertura.",
                        chartBytes,
                        "coverage.png"
                );
                return ResponseEntity.ok("E-mail enviado com gráfico de coverage em anexo!");
            }
            else {
                emailService.sendEmail(emailRequest.getTo(), subject, message);
                return ResponseEntity.ok("E-mail enviado com sucesso!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar e-mail: " + e.getMessage());
        }
    }

    @PostMapping("/send-email-worklog")
    @ResponseBody
    public ResponseEntity<String> sendReportEmailWorklog(@RequestBody EmailRequest emailRequest, HttpSession session, String issueKey) {
        String subject = emailRequest.getSubject();
        String message = emailRequest.getMessage();

        try {
            if (subject != null && issueKey != null) {
                @SuppressWarnings("unchecked")
                List<IssueDTO> issues = (List<IssueDTO>) session.getAttribute("issuesList");
                if (issues == null || issues.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma lista de issues encontrada na sessão.");
                }

                IssueDTO issue = issues.stream().filter(x -> x.getKey().trim().equalsIgnoreCase(issueKey.trim())).findAny().orElse(null);
                if (issue == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue não encontrada.");
                }

                byte[] chartBytes = worklogChartService.generateWorklogChartAsBytes(issue);

                StringBuilder sb = new StringBuilder(message);

                emailService.sendEmailWithAttachment(
                        emailRequest.getTo(),
                        subject,
                        sb.toString() + "\n\nSegue em anexo o gráfico do Worklog.",
                        chartBytes,
                        "worklog-" + issue.getKey() + ".png"
                );
                return ResponseEntity.ok("E-mail enviado com gráfico de worklog em anexo!");
            } else {
                emailService.sendEmail(emailRequest.getTo(), subject, message);
                return ResponseEntity.ok("E-mail enviado com sucesso!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}
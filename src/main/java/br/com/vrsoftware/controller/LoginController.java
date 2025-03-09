package br.com.vrsoftware.controller;

import br.com.vrsoftware.dto.AuthCredentialsDTO;
import br.com.vrsoftware.exceptions.AuthenticationException;
import br.com.vrsoftware.service.security.SecureCredentialsLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/welcome")
    public String welcome(Model model) {
        return "welcome";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam(value = "loginType") String loginType,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "jiraToken", required = false) String jiraToken,
            @RequestParam(value = "masterPasswordForm", required = false) String masterPasswordForm,
            @RequestParam(value = "masterPasswordFile", required = false) String masterPasswordFile,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            if ("form".equals(loginType)) {
                // Process form-based login
                boolean authenticated = authenticateWithFormCredentials(email, jiraToken, masterPasswordForm);
                if (authenticated) {
                    // Set user in session and redirect to dashboard
                    return "redirect:/welcome";
                } else {
                    model.addAttribute("message", "Invalid email or JIRA token");
                    model.addAttribute("alertClass", "alert-danger");
                    return "login";
                }
            } else if ("file".equals(loginType)) {
                // Process file-based login
                AuthCredentialsDTO credentials = SecureCredentialsLoaderService.loadSecureCredentials(masterPasswordFile);

                if (Objects.nonNull(credentials)) {
                    // Set user in session and redirect to dashboard
                    return "redirect:/welcome";
                } else {
                    model.addAttribute("message", "Failed to authenticate with file credentials");
                    model.addAttribute("alertClass", "alert-danger");
                    return "login";
                }
            } else {
                model.addAttribute("message", "Invalid login method");
                model.addAttribute("alertClass", "alert-danger");
                return "login";
            }
        } catch (AuthenticationException e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("alertClass", "alert-danger");
            return "login";
        } catch (Exception e) {
            model.addAttribute("message", "An error occurred during login: " + e.getMessage());
            model.addAttribute("alertClass", "alert-danger");
            return "login";
        }
    }

    private boolean authenticateWithFormCredentials(String email, String jiraToken, String masterPassword) {
        AuthCredentialsDTO credentials = new AuthCredentialsDTO(email, jiraToken);
        return SecureCredentialsLoaderService.loadSecureCredentials(masterPassword).equals(credentials);
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        // Implement registration page
        return "register";
    }
}

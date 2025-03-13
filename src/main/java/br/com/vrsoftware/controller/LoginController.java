package br.com.vrsoftware.controller;

import br.com.vrsoftware.dto.AuthCredentialsDTO;
import br.com.vrsoftware.exceptions.AuthenticationException;
import br.com.vrsoftware.service.security.SecureCredentialsLoaderService;
import br.com.vrsoftware.service.security.SecureProxyLoaderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Properties;

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
            @RequestParam(value = "proxyHost", required = false) String proxyHost,
            @RequestParam(value = "proxyPort", required = false) String proxyPort,
            @RequestParam(value = "proxyUser", required = false) String proxyUser,
            @RequestParam(value = "proxyPassword", required = false) String proxyPassword,
            Model model,
            HttpSession session) {

        try {
            AuthCredentialsDTO credentials = null;
            Properties proxyProperties = null;

            if ("form".equals(loginType)) {
                // Process form-based login
                boolean authenticated = authenticateWithFormCredentials(email, jiraToken, masterPasswordForm);
                if (authenticated) {
                    credentials = new AuthCredentialsDTO(email, jiraToken);
                    proxyProperties = new Properties();
                    proxyProperties.setProperty("http.proxyHost", proxyHost);
                    proxyProperties.setProperty("http.proxyPort", proxyPort);
                    proxyProperties.setProperty("https.proxyHost", proxyHost);
                    proxyProperties.setProperty("https.proxyPort", proxyPort);
                    proxyProperties.setProperty("http.proxyUser", proxyUser);
                    proxyProperties.setProperty("http.proxyPassword", proxyPassword);
                } else {
                    model.addAttribute("message", "Invalid email or JIRA token");
                    model.addAttribute("alertClass", "alert-danger");
                    return "login";
                }
            } else if ("file".equals(loginType)) {
                // Process file-based login
                credentials = SecureCredentialsLoaderService.loadSecureCredentials(masterPasswordFile);
                proxyProperties = SecureProxyLoaderService.loadSecureProxySettings(masterPasswordFile);
            } else {
                model.addAttribute("message", "Invalid login method");
                model.addAttribute("alertClass", "alert-danger");
                return "login";
            }

            // Store credentials and proxy settings in session
            session.setAttribute("userCredentials", credentials);
            session.setAttribute("proxyProperties", proxyProperties);

            // Apply proxy settings to system properties
            SecureProxyLoaderService.applyProxySettings(proxyProperties);

            // Redirect to dashboard
            return "redirect:/dashboard";
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
}

package br.com.vrsoftware.controller;

import br.com.vrsoftware.dto.AuthCredentialsDTO;
import br.com.vrsoftware.dto.LoginValuesDTO;
import br.com.vrsoftware.dto.SignUpValuesDTO;
import br.com.vrsoftware.exceptions.AuthenticationException;
import br.com.vrsoftware.service.security.SecureCredentialsLoaderService;
import br.com.vrsoftware.service.security.SetupCredentialsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final SetupCredentialsService setupCredentialsService;

    public LoginController(SetupCredentialsService setupCredentialsService) {
        this.setupCredentialsService = setupCredentialsService;
    }

    @GetMapping({"/", "/login"})
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/signup")
    public String signup(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "jiraToken", required = false) String jiraToken,
            @RequestParam(value = "masterPassword", required = false) String masterPassword,
            Model model,
            HttpSession session) {

        try {
            SignUpValuesDTO signUpFormValues = new SignUpValuesDTO(email, jiraToken, masterPassword);

            setupCredentialsService.signup(signUpFormValues);

            model.addAttribute("message", "Registration successful!");
            model.addAttribute("alertClass", "alert-success");
            return "login";
        } catch (Exception e) {
            model.addAttribute("message", "An error occurred during sign up: " + e.getMessage());
            model.addAttribute("alertClass", "alert-danger");
            return "login";
        }
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "masterPassword", required = false) String masterPassword,
            Model model,
            HttpSession session) {

        try {
            LoginValuesDTO loginValues = new LoginValuesDTO(email, masterPassword);

            // Process file-based login
            AuthCredentialsDTO authCredentials = SecureCredentialsLoaderService.loadSecureCredentials(loginValues);

            // Store credentials in session
            session.setAttribute("userCredentials", authCredentials);

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
}

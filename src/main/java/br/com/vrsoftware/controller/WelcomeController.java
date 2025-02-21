package br.com.vrsoftware.controller;

import br.com.vrsoftware.dto.template.WelcomeDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Controller
public class WelcomeController {

    @GetMapping("/")
    public String welcome(Model model) {
        String title = "Welcome to VR Software!";
        String message = "This is a simple example of Thymeleaf template rendering.";
        String currentUser = "rafael-brito";

        // Get UTC time and format it exactly as requested
        String currentDateTime = LocalDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        var welcome = new WelcomeDTO(title, message, currentUser, currentDateTime);
        model.addAttribute("welcome", welcome);

        return "welcome";
    }
}

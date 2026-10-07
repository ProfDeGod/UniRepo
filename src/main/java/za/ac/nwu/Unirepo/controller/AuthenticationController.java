package za.ac.nwu.Unirepo.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import za.ac.nwu.Unirepo.model.AppUser;
import za.ac.nwu.Unirepo.repository.AppUserRepository;

@Controller
public class AuthenticationController {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(
            AppUserRepository repository,
            PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        if (repository.existsByUsername(username)) {
            model.addAttribute(
                    "error",
                    "Username already exists."
            );

            return "register";
        }

        AppUser user = new AppUser();

        user.setUsername(username);

        // Encrypt the password before saving it
        user.setPassword(
                passwordEncoder.encode(password)
        );

        // Default role
        user.setRole("USER");

        repository.save(user);

        return "redirect:/login?registered";
    }
}
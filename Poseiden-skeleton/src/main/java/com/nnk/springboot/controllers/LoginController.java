package com.nnk.springboot.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {
    /**
     * LOGIN & ERROR - public page
     */
    @GetMapping("")
    public String indexLogin() {
        return "redirect:/login";
    }
    @GetMapping("login")
    public String login() {
        return "login";
    }

    /**
     * HOME PAGE - secured page
     */
    @GetMapping("home")
    @Secured({"ADMIN", "USER"})
    public String home(Model model) {
        return "home";
    }
}

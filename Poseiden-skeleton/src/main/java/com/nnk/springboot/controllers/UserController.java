package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class UserController {
    private final String SECURED_URL = "user";
    @Autowired
    private UserService userService;

    /**
     * LOGIN
     */
    @RequestMapping("/")
    public String root() {
        return "redirect:/login";
    }
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * HOMEPAGE
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.findAll());
        return SECURED_URL + "/list";
    }

    @GetMapping("/user/add")
    public String addUser(User bid) {
        return SECURED_URL + "/add";
    }
    @PostMapping("/user/validate")
    public String validate(
            @Valid User user,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userService.save(user);

            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        }
        return SECURED_URL + "/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(
            @PathVariable("id") Integer id,
            Model model) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setPassword("");

        model.addAttribute("user", user);
        return SECURED_URL + "/update";
    }
    @PostMapping("/user/update/{id}")
    public String updateUser(
            @PathVariable("id") Integer id,
            @Valid User user,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return SECURED_URL + "/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        userService.save(user);

        model.addAttribute("users", userService.findAll());
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(
            @PathVariable("id") Integer id,
            Model model) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userService.delete(user);

        model.addAttribute("users", userService.findAll());
        return "redirect:/user/list";
    }
}

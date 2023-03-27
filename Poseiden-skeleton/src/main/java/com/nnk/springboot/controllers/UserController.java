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
import java.util.Optional;

@Controller
@RequestMapping("/user/")
public class UserController {
    private final String SECURED_URL = "user";
    @Autowired
    private UserService userService;

    /**
     * USERS LIST
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.findAll());
        return SECURED_URL + "/list";
    }

    /* -- Add user */
    @GetMapping("/user/add")
    public String addUser(Model model) {
        return SECURED_URL + "/add";
    }
    @PostMapping("/user/validate")
    public String validate(
            @Valid User newUser,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            newUser.setPassword(encoder.encode(newUser.getPassword()));
            userService.save(newUser);

            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        }
        return SECURED_URL + "/add";
    }

    /* -- Update user */
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
            @Valid User updateUser,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            Optional<User> savedUser = userService.findById(id);

            if (updateUser != null && savedUser.isPresent()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                updateUser.setPassword(encoder.encode(updateUser.getPassword()));
                updateUser.setId(id);
                userService.save(updateUser);

                model.addAttribute("users", userService.findAll());
                return "redirect:/user/list";
            }
        }
        return SECURED_URL + "/update";
    }

    /* -- Delete user */
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

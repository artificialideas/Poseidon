package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
@Secured("ADMIN")
@CommonsLog
public class UserController {
    private final String SECURED_URL = "user";
    @Autowired
    private UserService userService;

    /**
     * USERS LIST
     */
    @GetMapping("list")
    public String home(Model model) {
        model.addAttribute("users", userService.findAll());
        return SECURED_URL + "/list";
    }

    /* -- Add user */
    @GetMapping("add")
    public String addUser(User user) {
        return SECURED_URL + "/add";
    }
    @PostMapping("validate")
    public String validate(
            @Valid User newUser,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            newUser.setPassword(encoder.encode(newUser.getPassword()));
            userService.save(newUser);

            log.info("A new user with id "+newUser.getId()+" has been created.");
            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        }
        log.error("The new user has not been created due to form errors.");
        return SECURED_URL + "/add";
    }

    /* -- Update user */
    @GetMapping("update/{id}")
    public String showUpdateForm(
            @PathVariable("id") Integer id,
            Model model) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setPassword("");

        model.addAttribute("user", user);
        return SECURED_URL + "/update";
    }
    @PostMapping("update/{id}")
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

                log.info("User with id "+id+" has been updated.");
                model.addAttribute("users", userService.findAll());
                return "redirect:/user/list";
            }
        }
        log.error("User with id "+id+" has not been updated due to form errors.");
        return SECURED_URL + "/update";
    }

    /* -- Delete user */
    @GetMapping("delete/{id}")
    public String deleteUser(
            @PathVariable("id") Integer id,
            Model model) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userService.delete(user);

        log.info("User with id "+id+" has been deleted.");
        model.addAttribute("users", userService.findAll());
        return "redirect:/user/list";
    }
}

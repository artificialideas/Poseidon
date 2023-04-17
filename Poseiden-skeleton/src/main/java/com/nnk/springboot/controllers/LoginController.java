package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;

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

    /*@GetMapping("error")
    public ModelAndView error() {
        ModelAndView mav = new ModelAndView();
        String errorMessage= "You are not authorized for the requested data.";

        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("403");
        return mav;
    }*/

    /**
     * HOME PAGE - secured page
     */
    @GetMapping("home")
    @RolesAllowed({"ADMIN", "USER"})
    public String home(Model model) {
        return "home";
    }
}

package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/rating/")
@RolesAllowed({"ADMIN", "USER"})
public class RatingController {
    private final String SECURED_URL = "rating";
    @Autowired
    private RatingService ratingService;

    @GetMapping("list")
    public String home(Model model) {
        model.addAttribute("rating", ratingService.findAll());
        return SECURED_URL + "/list";
    }

    /* -- Add rating */
    @GetMapping("add")
    public String addRating(Rating rating) {
        return SECURED_URL + "/add";
    }
    @PostMapping("validate")
    public String validate(
            @Valid Rating newRating,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            ratingService.save(newRating);

            model.addAttribute("rating", ratingService.findAll());
            return "redirect:/rating/list";
        }
        return SECURED_URL + "/add";
    }

    /* -- Update rating */
    @GetMapping("update/{id}")
    public String showUpdateForm(
            @PathVariable("id") Integer id,
            Model model) {
        Rating rating = ratingService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rating Id:" + id));

        model.addAttribute("rating", rating);
        return SECURED_URL + "/update";
    }
    @PostMapping("update/{id}")
    public String updateRating(
            @PathVariable("id") Integer id,
            @Valid Rating updateRating,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            Optional<Rating> savedRating = ratingService.findById(id);

            if (updateRating != null && savedRating.isPresent()) {
                updateRating.setId(id);
                ratingService.save(updateRating);

                model.addAttribute("rating", ratingService.findAll());
                return "redirect:/rating/list";
            }
        }
        return SECURED_URL + "/update";
    }

    /* -- Delete rating */
    @GetMapping("delete/{id}")
    public String deleteRating(
            @PathVariable("id") Integer id,
            Model model) {
        Rating rating = ratingService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rating Id:" + id));
        ratingService.delete(rating);

        model.addAttribute("rating", ratingService.findAll());
        return "redirect:/rating/list";
    }
}

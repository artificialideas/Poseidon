package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.service.CurvePointService;
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
@RequestMapping("/curvePoint/")
@RolesAllowed({"ADMIN", "USER"})
public class CurveController {
    private final String SECURED_URL = "curvePoint";
    @Autowired
    private CurvePointService curvePointService;

    @GetMapping("list")
    public String home(Model model) {
        model.addAttribute("curvePoint", curvePointService.findAll());
        return SECURED_URL + "/list";
    }

    /* -- Add bid */
    @GetMapping("add")
    public String addCurve(CurvePoint curvePoint) {
        return SECURED_URL + "/add";
    }
    @PostMapping("validate")
    public String validate(
            @Valid CurvePoint newCurvePoint,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            curvePointService.save(newCurvePoint);

            model.addAttribute("curvePoint", curvePointService.findAll());
            return "redirect:/curvePoint/list";
        }
        return SECURED_URL + "/add";
    }

    /* -- Update bid */
    @GetMapping("update/{id}")
    public String showUpdateForm(
            @PathVariable("id") Integer id,
            Model model) {
        CurvePoint curvePoint = curvePointService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid curve Id:" + id));

        model.addAttribute("curvePoint", curvePoint);
        return SECURED_URL + "/update";
    }
    @PostMapping("update/{id}")
    public String updateCurvePoint(
            @PathVariable("id") Integer id,
            @Valid CurvePoint updateCurvePoint,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            Optional<CurvePoint> savedCurvePoint = curvePointService.findById(id);

            if (updateCurvePoint != null && savedCurvePoint.isPresent()) {
                updateCurvePoint.setId(id);
                curvePointService.save(updateCurvePoint);

                model.addAttribute("curvePoint", curvePointService.findAll());
                return "redirect:/curvePoint/list";
            }
        }
        return SECURED_URL + "/update";
    }

    /* -- Delete bid */
    @GetMapping("delete/{id}")
    public String deleteBid(
            @PathVariable("id") Integer id,
            Model model) {
        CurvePoint curvePoint = curvePointService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid bid Id:" + id));
        curvePointService.delete(curvePoint);

        model.addAttribute("curvePoint", curvePointService.findAll());
        return "redirect:/curvePoint/list";
    }
}

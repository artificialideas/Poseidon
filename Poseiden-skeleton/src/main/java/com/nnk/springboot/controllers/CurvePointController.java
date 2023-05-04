package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.service.CurvePointService;
import lombok.extern.apachecommons.CommonsLog;
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
@CommonsLog
public class CurvePointController {
    private final String SECURED_URL = "curvePoint";
    @Autowired
    private CurvePointService curvePointService;

    @GetMapping("list")
    public String home(Model model) {
        model.addAttribute("curvePoint", curvePointService.findAll());
        return SECURED_URL + "/list";
    }

    /* -- Add curve point */
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

            log.info("A new curve point with id "+newCurvePoint.getId()+" has been created.");
            model.addAttribute("curvePoint", curvePointService.findAll());
            return "redirect:/curvePoint/list";
        }
        log.error("The new curve point has not been created due to form errors.");
        return SECURED_URL + "/add";
    }

    /* -- Update curve point */
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

                log.info("Curve point with id "+id+" has been updated.");
                model.addAttribute("curvePoint", curvePointService.findAll());
                return "redirect:/curvePoint/list";
            }
        }
        log.error("Curve point with id "+id+" has not been updated due to form errors.");
        return SECURED_URL + "/update";
    }

    /* -- Delete curve point */
    @GetMapping("delete/{id}")
    public String deleteCurvePoint(
            @PathVariable("id") Integer id,
            Model model) {
        CurvePoint curvePoint = curvePointService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid curve Id:" + id));
        curvePointService.delete(curvePoint);

        log.info("Curve point with id "+id+" has been deleted.");
        model.addAttribute("curvePoint", curvePointService.findAll());
        return "redirect:/curvePoint/list";
    }
}

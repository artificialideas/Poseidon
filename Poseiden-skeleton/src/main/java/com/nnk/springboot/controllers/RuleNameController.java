package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.service.RuleNameService;
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
@RequestMapping("/ruleName/")
@RolesAllowed({"ADMIN", "USER"})
public class RuleNameController {
    private final String SECURED_URL = "ruleName";
    @Autowired
    private RuleNameService ruleNameService;

    @GetMapping("list")
    public String home(Model model) {
        model.addAttribute("ruleName", ruleNameService.findAll());
        return SECURED_URL + "/list";
    }

    /* -- Add rule */
    @GetMapping("add")
    public String addRule(RuleName ruleName) {
        return SECURED_URL + "/add";
    }
    @PostMapping("validate")
    public String validate(
            @Valid RuleName newRule,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            ruleNameService.save(newRule);

            model.addAttribute("ruleName", ruleNameService.findAll());
            return "redirect:/ruleName/list";
        }
        return SECURED_URL + "/add";
    }

    /* -- Update rule */
    @GetMapping("update/{id}")
    public String showUpdateForm(
            @PathVariable("id") Integer id,
            Model model) {
        RuleName ruleName = ruleNameService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rule Id:" + id));

        model.addAttribute("ruleName", ruleName);
        return SECURED_URL + "/update";
    }
    @PostMapping("update/{id}")
    public String updateRule(
            @PathVariable("id") Integer id,
            @Valid RuleName updateRule,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            Optional<RuleName> savedRule = ruleNameService.findById(id);

            if (updateRule != null && savedRule.isPresent()) {
                updateRule.setId(id);
                ruleNameService.save(updateRule);

                model.addAttribute("ruleName", ruleNameService.findAll());
                return "redirect:/ruleName/list";
            }
        }
        return SECURED_URL + "/update";
    }

    /* -- Delete rule */
    @GetMapping("delete/{id}")
    public String deleteRule(
            @PathVariable("id") Integer id,
            Model model) {
        RuleName ruleName = ruleNameService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rule Id:" + id));
        ruleNameService.delete(ruleName);

        model.addAttribute("ruleName", ruleNameService.findAll());
        return "redirect:/ruleName/list";
    }
}

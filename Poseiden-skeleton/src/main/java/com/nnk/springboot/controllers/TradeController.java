package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.service.TradeService;
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

@Controller
@RequestMapping("/trade/")
@RolesAllowed({"ADMIN", "USER"})
@CommonsLog
public class TradeController {
    private final String SECURED_URL = "trade";
    @Autowired
    private TradeService tradeService;

    @GetMapping("list")
    public String home(Model model) {
        model.addAttribute("trade", tradeService.findAll());
        return SECURED_URL + "/list";
    }

    /* -- Add trade */
    @GetMapping("add")
    public String addTrade(Trade trade) {
        return SECURED_URL + "/add";
    }
    @PostMapping("validate")
    public String validate(
            @Valid Trade newTrade,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            tradeService.save(newTrade);

            log.info("A new trade with id "+newTrade.getId()+" has been created.");
            model.addAttribute("trade", tradeService.findAll());
            return "redirect:/trade/list";
        }
        log.error("The new trade has not been created due to form errors.");
        return SECURED_URL + "/add";
    }

    /* -- Update trade */
    @GetMapping("update/{id}")
    public String showUpdateForm(
            @PathVariable("id") Integer id,
            Model model) {
        Trade trade = tradeService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));

        model.addAttribute("trade", trade);
        return SECURED_URL + "/update";
    }
    @PostMapping("update/{id}")
    public String updateTrade(
            @PathVariable("id") Integer id,
            @Valid Trade updateTrade,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            Trade savedTrade = tradeService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));

            if (updateTrade != null && savedTrade != null) {
                updateTrade.setId(id);
                tradeService.save(updateTrade);

                log.info("Trade with id "+id+" has been updated.");
                model.addAttribute("trade", tradeService.findAll());
                return "redirect:/trade/list";
            }
        }
        log.error("Trade with id "+id+" has not been updated due to form errors.");
        return SECURED_URL + "/update";
    }

    /* -- Delete trade */
    @GetMapping("delete/{id}")
    public String deleteTrade(
            @PathVariable("id") Integer id,
            Model model) {
        Trade trade = tradeService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));
        tradeService.delete(trade);

        log.info("Trade with id "+id+" has been deleted.");
        model.addAttribute("trade", tradeService.findAll());
        return "redirect:/trade/list";
    }
}

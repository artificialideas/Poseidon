package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.service.TradeService;
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
    @PostMapping("add")
    public String addTrade(
            @Valid Trade newTrade,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            tradeService.save(newTrade);

            model.addAttribute("trade", tradeService.findAll());
            return "redirect:/trade/list";
        }
        return SECURED_URL + "/add";
    }

    /* -- Update trade */
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

                model.addAttribute("trade", tradeService.findAll());
                return "redirect:/trade/list";
            }
        }
        return SECURED_URL + "/update";
    }

    /* -- Delete trade */
    @GetMapping("delete/{id}")
    public String deleteTrade(
            @PathVariable("id") Integer id,
            Model model) {
        Trade trade = tradeService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));
        tradeService.delete(trade);

        model.addAttribute("trade", tradeService.findAll());
        return "redirect:/trade/list";
    }
}

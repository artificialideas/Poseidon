package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.service.BidListService;
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
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/bidList/")
@RolesAllowed({"ADMIN", "USER"})
@CommonsLog
public class BidListController {
    private final String SECURED_URL = "bidList";
    @Autowired
    private BidListService bidListService;

    @GetMapping("list")
    public String home(Model model) {
        model.addAttribute("bidList", bidListService.findAll());
        return SECURED_URL + "/list";
    }

    /* -- Add bid */
    @GetMapping("add")
    public String addBid(BidList bid) {
        return SECURED_URL + "/add";
    }
    @PostMapping("validate")
    public String validate(
            @Valid BidList newBid,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            bidListService.save(newBid);

            log.info("A new bid with id "+newBid.getId()+" has been created.");
            model.addAttribute("bidList", bidListService.findAll());
            return "redirect:/bidList/list";
        }
        log.error("The new bid has not been due to form errors.");
        return SECURED_URL + "/add";
    }

    /* -- Update bid */
    @GetMapping("update/{id}")
    public String showUpdateForm(
            @PathVariable("id") Integer id,
            Model model) {
        BidList bidList = bidListService.findById(id);

        if (bidList != null) {
            model.addAttribute("bidList", bidList);
            return SECURED_URL + "/update";
        }
        log.error("Invalid bid Id: " + id);
        return "redirect:/bidList/list";
    }
    @PostMapping("update/{id}")
    public String updateBid(
            @PathVariable("id") Integer id,
            @Valid BidList updateBid,
            BindingResult result,
            Model model) {
        if (!result.hasErrors()) {
            BidList savedBid = bidListService.findById(id);

            if (updateBid != null && savedBid != null) {
                updateBid.setId(id);
                bidListService.save(updateBid);

                log.info("Bid with id "+id+" has been updated.");
                model.addAttribute("bidList", bidListService.findAll());
                return "redirect:/bidList/list";
            }
        }
        log.error("Bid with id "+id+" has not been updated due to form errors.");
        return SECURED_URL + "/update";
    }

    /* -- Delete bid */
    @GetMapping("delete/{id}")
    public String deleteBid(
            @PathVariable("id") Integer id,
            Model model) {
        BidList bid = bidListService.findById(id);
        List<BidList> bidIds = new ArrayList<>();
            bidIds.add(bid);

        if (bid != null) {
            bidListService.delete(bidIds);
            log.info("Bid with id "+id+" has been deleted.");
            model.addAttribute("bidList", bidListService.findAll());
        } else
            log.error("Invalid bid Id: " + id);

        return "redirect:/bidList/list";
    }
}

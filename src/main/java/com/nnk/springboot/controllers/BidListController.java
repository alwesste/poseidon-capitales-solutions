package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.impl.BidListService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class BidListController {

    private static final Logger logger = LogManager.getLogger(BidListController.class);

    @Autowired
    private BidListService bidListService;

    /**
     *
     * @param model
     * @param authentication
     * @return la vue bidList/list
     */
    @RequestMapping("/bidList/list")
    public String home(Model model, Authentication authentication)
    {
        model.addAttribute("bidLists", bidListService.findAll());
        model.addAttribute("username", authentication.getName());
        return "bidList/list";
    }

    /**
     *
     * @param bid
     * @return la vue bidList/add
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    /**
     *
     * @param bid
     * @param result
     * @param model
     * @return la vue bidList/list apres un envoi d'un objet bid
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        if(result.hasErrors()) {
            logger.warn("Erreur lors de la validation de BidList {} : {}",
                    bid.getAccount(), result.getAllErrors());
            return "bidList/add";
        }

        bidListService.save(bid);
        return "redirect:/bidList/list";
    }

    /**
     *
     * @param id
     * @param model
     * @return la vue bidList/update
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList updateBidList = bidListService.findById(id);

        model.addAttribute("bidList", updateBidList);
        return "bidList/update";
    }

    /**
     *
     * @param id
     * @param bidList
     * @param result
     * @param model
     * @return la vue bidList/list apres mise a jour du bid
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("Erreur de lors de la mise a jour de Bidlist {} : {}",
                    bidList.getAccount(), result.getAllErrors());
            model.addAttribute("bidLists", bidList);
            return "bidList/list";
        }

        BidList newBidList = bidListService.findById(id);
        newBidList.setAccount(bidList.getAccount());
        newBidList.setType(bidList.getType());
        newBidList.setBidQuantity(bidList.getBidQuantity());
        bidListService.save(newBidList);
        return "redirect:/bidList/list";
    }

    /**
     *
     * @param id
     * @param model
     * @return la vue bidList/list apres suppression d'un bid via son Id.
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        bidListService.delete(id);
        return "redirect:/bidList/list";
    }
}

package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.impl.TradeService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;


@Controller
public class TradeController {

    private static final Logger logger = LogManager.getLogger(TradeController.class);

    @Autowired
    private TradeService tradeService;

    /**
     *
     * @param model
     * @param authentication
     * @return la vue trade/list
     */
    @RequestMapping("/trade/list")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("trades", tradeService.findAll());
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        return "trade/list";
    }

    /**
     *
     * @param bid
     * @return la vue trade/add
     */
    @GetMapping("/trade/add")
    public String addUser(Trade bid) {
        return "trade/add";
    }

    /**
     *
     * @param trade
     * @param result
     * @param model
     * @return la vue trade/list apres verification de l'objet trade
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("Erreur de la validation pour le trade {} : {}",
                    trade.getAccount(), result.getAllErrors());
            return "trade/add";
        }

        logger.info("Trade completer avec le compte {}", trade.getAccount());
        tradeService.save(trade);
        return "redirect:/trade/list";
    }

    /**
     *
     * @param id
     * @param model
     * @return la vue trade/update via l'id de trade
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Trade updateTrade = tradeService.findById(id);
        model.addAttribute("trade", updateTrade);
        return "trade/update";
    }

    /**
     *
     * @param id
     * @param trade
     * @param result
     * @param model
     * @return la vue trade/list apres la mise a jour de trade.
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                              BindingResult result, Model model) {
        if (!result.hasErrors()) {
            Trade newTrade = tradeService.findById(id);
            newTrade.setAccount(trade.getAccount());
            newTrade.setType(trade.getType());
            newTrade.setBuyQuantity(trade.getBuyQuantity());
            tradeService.save(newTrade);
            return "redirect:/trade/list";
        }
        return "trade/list";
    }

    /**
     *
     * @param id
     * @param model
     * @return la vue trade/list apres suppression du trade via son Id
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        tradeService.delete(id);
        return "redirect:/trade/list";
    }
}

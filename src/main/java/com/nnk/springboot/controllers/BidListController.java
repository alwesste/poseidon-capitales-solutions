package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.impl.BidListService;
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

/**
 * Controleur gerant les interactions utilisateur avec l'entite BidList.
 * Ce contrôleur permet d'afficher la liste des offres d'achat (Bids),
 * d'en ajouter de nouvelles, de les modifier et de les supprimer.
 * Il intègre également des vérifications de sécurité pour l'affichage conditionnel
 * dans les vues Thymeleaf (via l'attribut isAdmin). */
@Controller
public class BidListController {

    private static final Logger logger = LogManager.getLogger(BidListController.class);

    @Autowired
    private BidListService bidListService;

    /**
     * Affiche la liste complete des BidList
     * @param model
     * @param authentication
     * @return la vue bidList/list
     */
    @RequestMapping("/bidList/list")
    public String home(Model model, Authentication authentication)
    {
        model.addAttribute("bidLists", bidListService.findAll());
        model.addAttribute("username", authentication.getName());

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        return "bidList/list";
    }

    /**
     * Affiche le formulaire d'ajout d'une nouvelle offre.
     * @param bid
     * @return la vue bidList/add
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    /**
     * Valide et enregistre une nouvelle offre dans la base de données.
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
     * Affiche le formulaire de mise à jour pour une offre spécifique
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
     * Traite la requête de mise à jour d'une offre existante
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
     * Supprime une offre de la base de donnéee
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

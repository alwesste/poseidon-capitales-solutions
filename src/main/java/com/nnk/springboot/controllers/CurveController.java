package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.impl.CurvePointService;
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
 * Controleur gerant les interactions utilisateur avec l'entite CurvePoint.
 * Fournit les points d'entrée pour l'affichage, l'ajout, la modification
 * et la suppression des données des CurvePoints.
 */
@Controller
public class CurveController {

    private static final Logger logger = LogManager.getLogger(CurveController.class);

    @Autowired
    private CurvePointService curvePointService;

    /**
     * Affiche la liste de tous les CurvePoint
     * @param model
     * @param authentication
     * @return la vue curvePoint/list
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("curvePoints", curvePointService.findAll());
        model.addAttribute("username", authentication.getName());
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        return "curvePoint/list";
    }

    /**
     * Affiche le formulaire pour l'ajout d'un nouveau point de courbe
     * @param model
     * @param curvePoint
     * @return la vue curvePoint/add
     */
    @GetMapping("/curvePoint/add")
    public String addBidForm(Model model, CurvePoint curvePoint) {

        return "curvePoint/add";
    }

    /**
     * Valide et enregistre un nouveau CurvePoint
     * @param curvePoint
     * @param result
     * @param model
     * @return la vue curvePoint/list apres validation des attributs de l'objet CurvePoint
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {

        if (result.hasErrors()) {
            logger.warn("Erreur lors de la validation de Curpoint id -> {} : {}",
                    curvePoint.getId(), result.getAllErrors());
            return "curvePoint/add";
        }
        curvePointService.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    /**
     * Affiche le formulaire de modification pour un CurvePoint
     * @param id
     * @param model
     * @return la vue curvePoint/list via l'id de curvePoint
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePoint = curvePointService.findById(id);
        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }

    /**
     * Traite la mise à jour d'un CurvePoint existant
     * @param id
     * @param curvePoint
     * @param result
     * @param model
     * @return la vue curvePoint/update apres mise a jour de curvePoint
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("Erreur lors de la mise a jour de curvePoint id -> {} : {}",
                    curvePoint.getCurveId(), result.getAllErrors());
            return "curvePoint/update";
        }

        CurvePoint newCurvePoint = curvePointService.findById(id);
        newCurvePoint.setCurveId(curvePoint.getCurveId());
        newCurvePoint.setTerm(curvePoint.getTerm());
        newCurvePoint.setValue(curvePoint.getValue());
        curvePointService.save(newCurvePoint);
        model.addAttribute("curvePoints", curvePointService.findAll());
        return "redirect:/curvePoint/list";
    }

    /**
     * Supprime un point de courbe de la base de données
     * @param id
     * @param model
     * @return la vue curvePoint/list apres suppression d'un curvePoint via son Id
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        curvePointService.delete(id);
        return "redirect:/curvePoint/list";
    }
}

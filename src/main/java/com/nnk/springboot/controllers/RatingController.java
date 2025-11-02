package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.impl.RatingService;
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
public class RatingController {

    private static final Logger logger = LogManager.getLogger(RatingController.class);

    @Autowired
    private RatingService ratingService;

    @RequestMapping("/rating/list")
    public String home(Model model, Authentication authentication) {

        model.addAttribute("ratings", ratingService.findAll());
        model.addAttribute("username", authentication.getName());

        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {

        if (result.hasErrors()) {
            logger.warn("Erreurs lors de la validation du rating {}, {}",
                    rating.getId(), result.getAllErrors());
            model.addAttribute("rating", ratingService.findAll());
            return "rating/add";
        }
        ratingService.save(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        Rating updateRating = ratingService.findById(id);

        model.addAttribute("rating", updateRating);
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                               BindingResult result, Model model) {

        if (result.hasErrors()) {
            logger.warn("Erreurs lors de la mise a jour du rating {} : {}",
                    rating.getId(), result.getAllErrors());
            return "rating/update";
        }

        Rating newRating = ratingService.findById(id);
        newRating.setMoodysRating(rating.getMoodysRating());
        newRating.setSandPRating(rating.getSandPRating());
        newRating.setFitchRating(rating.getFitchRating());
        newRating.setOrderNumber(rating.getOrderNumber());
        ratingService.save(newRating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {

        ratingService.delete(id);
        return "redirect:/rating/list";
    }
}

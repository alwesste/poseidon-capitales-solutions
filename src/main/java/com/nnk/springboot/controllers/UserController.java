package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.impl.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Contrôleur gérant l'administration des utilisateurs de l'application
 */
@Controller
@Transactional
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Affiche la liste de tous les utilisateurs enregistrés dans le système.
     * @param model
     * @return la vue user/list
     */
    @RequestMapping("/user/list")
    public String home(Model model)
    {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    /**
     * Affiche le formulaire de création pour un nouvel utilisateur.
     * @param user
     * @return la vue user/add
     */
    @GetMapping("/user/add")
    public String addUser(User user) {
        return "user/add";
    }

    /**
     * Valide et enregistre un nouvel utilisateur.
     * @param user
     * @param result
     * @param model
     * @return la vue user/list apres la verification de l objet User
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("Erreurs de validation pour l'utilisateur {}: {}",
                    user.getUsername(), result.getAllErrors());
            return "user/add";
        }
        try {
            userService.save(user);
            logger.info("Utilisateur créé avec succès: {}", user.getUsername());
            return "redirect:/user/list";

        } catch (Exception e) {
            logger.error("Erreur lors de la sauvegarde de l'utilisateur", e);
            return "user/add";
        }
    }

    /**
     * Affiche le formulaire de mise à jour pour un utilisateur existant.
     * @param id
     * @param model
     * @return la vue user/update via l id de user
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/update";
    }

    /**
     * Traite la mise à jour d'un utilisateur existant.
     * @param id
     * @param user
     * @param result
     * @param model
     * @return la vue user/list apres mise a jour de user
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }
        user.setId(id);
        userService.save(user);
        return "redirect:/user/list";
    }

    /**
     * Supprime un utilisateur du système.
     * @param id
     * @param model
     * @return la vue user/list apres suppression de l'objet user via son Id
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        userService.delete(id);
        return "redirect:/user/list";
    }
}

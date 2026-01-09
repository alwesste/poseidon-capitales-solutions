package com.nnk.springboot.controllers;

import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.impl.AuthService;
import com.nnk.springboot.services.impl.JWTService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controleur gerant le processus d'authentification des utilisateurs.
 * Ce contrôleur expose les points d'entrée pour l'affichage de la page de connexion,
 */
@Controller
@RequestMapping("app")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Affiche le formulaire de connexion.
     * @return la page de login
     */
    @GetMapping("login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

    /**
     * Traite la tentative de connexion utilisateur
     * @param username, le nom d'utilisateur saisi
     * @param password,lLe mot de passe saisi
     * @param httpServletResponse l'objet de reponse permettant de recuperer le cookie
     * @return la vue "home" apres generation d'un token jwt et en le placant dans un cookie
     */
    @PostMapping("signin")
    public String signIn(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse httpServletResponse) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        authService.authenticateUserWithJWT(authentication, httpServletResponse);
        return "redirect:/";
    }

    /**
     * Donne la liste des utilisateurs
     * @return la vue user/list apres avoir ajoute les utilisateurs sous le nom
     * "users" dans le model
     */
    @GetMapping("secure/article-details")
    public ModelAndView getAllUserArticles() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", userRepository.findAll());
        mav.setViewName("user/list");
        return mav;
    }

    /**
     * Affichage d'une page d'erreur personnalisee
     * @param model
     * @param authentication
     * @return une page d'erreur si une erreur est apparue
     */
    @GetMapping("error")
    public ModelAndView error(Model model, Authentication authentication) {
        ModelAndView mav = new ModelAndView();
        model.addAttribute("username", authentication.getName());
        String errorMessage = "You are not authorized for the requested data.";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("403");
        return mav;
    }
}

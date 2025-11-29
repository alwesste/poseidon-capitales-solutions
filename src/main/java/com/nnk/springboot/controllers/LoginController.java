package com.nnk.springboot.controllers;

import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.impl.JWTService;
import jakarta.servlet.http.Cookie;
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

@Controller
@RequestMapping("app")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     *
     * @return la page de login
     */
    @GetMapping("login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

    /**
     *
     * @param username
     * @param password
     * @param httpServletResponse
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

        String token = jwtService.generateToken(authentication);
        Cookie tokenCookie = new Cookie("jwt", token);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(24 * 60 * 60);
        httpServletResponse.addCookie(tokenCookie);
        return "redirect:/";
    }


    /**
     *
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
     *
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

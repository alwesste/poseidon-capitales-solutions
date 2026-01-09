package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.IAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
    private static final Logger logger = LogManager.getLogger(AuthService.class);

    @Autowired
    private JWTService jwtService;

    @Override
    public void authenticateUserWithJWT(User user, HttpServletResponse response) {
        try {
            // Generation du token JWT
            String token = jwtService.generateToken(user.getUsername());

            // Creation du cookie JWT
            Cookie jwtCookie = createJwtCookie(token);

            // Ajout du cookie à la réponse
            response.addCookie(jwtCookie);

            logger.info("Token JWT généré et cookie ajouté pour l'utilisateur: {}", user.getUsername());

        } catch (Exception e) {
            logger.error("Erreur lors de la génération du token JWT pour l'utilisateur: {}",
                    user.getUsername(), e);
            throw new RuntimeException("Impossible de générer le token d'authentification", e);
        }
    }

    @Override
    public void authenticateUserWithJWT(Authentication authentication, HttpServletResponse response) {
        try {
            String token = jwtService.generateToken(authentication);
            Cookie jwtCookie = createJwtCookie(token);
            response.addCookie(jwtCookie);
            logger.info("Token JWT généré et cookie ajouté pour l'utilisateur: {}",
                    authentication.getName());

        } catch (Exception e) {
            logger.error("Erreur lors de la génération du token JWT pour l'utilisateur: {}",
                    authentication.getName(), e);
            throw new RuntimeException("Impossible de générer le token d'authentification", e);
        }
    }

    /**
     *
     * @param token a ajouter dans un cookie
     * @return un cookie
     */
    private Cookie createJwtCookie(String token) {
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);  // Protection XSS
        jwtCookie.setSecure(false);    // Mettre à true en production avec HTTPS
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60); // 24 heures
        return jwtCookie;
    }


}
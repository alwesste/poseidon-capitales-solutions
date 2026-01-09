package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;


/**
 * Gere la creation des cookie pour le jwt et authentification
 */
public interface IAuthService {

    /**
     * Genere un token JWT et l'ajoute comme cookie dans la réponse HTTP
     * @param user l'utilisateur pour lequel générer le token
     * @param response
     */
    void authenticateUserWithJWT(User user, HttpServletResponse response);

    /**
     * Genere un token JWT et l'ajoute comme cookie dans la reponse HTTP
     * @param authentication l'objet Authentication contenant les informations de l'utilisateur
     * @param response la réponse HTTP pour ajouter le cookie
     */
    void authenticateUserWithJWT(Authentication authentication, HttpServletResponse response);
}

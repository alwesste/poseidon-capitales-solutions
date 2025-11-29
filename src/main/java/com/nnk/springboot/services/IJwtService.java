package com.nnk.springboot.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {
    /**
     *
     * @param authentication, les infortions de l'utilisateur authentifie
     * @return un token avec le nom, date d'expiration, cle hasher
     */
    String generateToken(Authentication authentication);

    /**
     *
     * @param token, le Token JWT a verifier
     * @return le username associe au token jwt
     */
    String extractUserName(String token);

    /**
     * Vérifie si un token JWT est valide pour un utilisateur donné.
     *
     * @param token le token JWT à vérifier
     * @param userDetails les informations de l'utilisateur pour la comparaison
     * @return true si le token est valide et correspond à l'utilisateur, false sinon
     */
    boolean validateToken(String token, UserDetails userDetails);
}

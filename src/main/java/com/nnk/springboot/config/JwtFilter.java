package com.nnk.springboot.config;

import com.nnk.springboot.services.impl.CustomUserDetailsService;
import com.nnk.springboot.services.impl.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtre la securite intercepte chaque requete HTTP pour verifier la presence et la validite d'un jeton
 *  JWT valide
 *  OncePerRequest garantit une execution par requete
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Filtre qui permet de valide si JWT est present dans les coolkies
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        String username = null;

        // Recupere le cookie et le place dans token en fonction de son nom "jwt"
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            try {
                //extrait le username du token
                username = jwtService.extractUserName(token);

                // Verifie si l'utilisateur est deja authentifie
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // on recupere l'utilisateur depuis la BDD
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                    // Verifie si kle token est valide (signature jwt, date expiration, nom du user)
                    if (jwtService.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                //Creation du token d'authentification spring security
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        //Declare a spring que l'utilisateur est authentifie
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                System.err.println("La validation du JWT a echoue: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
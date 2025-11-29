package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;

import java.util.List;

/**
 * Service pour gérer les opérations sur les Ratings.
 * Fournit les méthodes CRUD pour manipuler les entités Rating.
 */
public interface IRatingService {

    /**
     * Récupère toutes les entités Rating existantes.
     *
     * @return une liste de Rating, vide si aucune n'existe
     */
    List<Rating> findAll();

    /**
     * Enregistre une nouvelle entité Rating ou met à jour une existante.
     *
     * @param rating l'entité Rating à sauvegarder
     * @return la Rating sauvegardée avec son ID généré si nécessaire
     */
    Rating save(Rating rating);

    /**
     * Cherche une entité Rating par son identifiant.
     *
     * @param id l'identifiant de la Rating
     * @return la Rating correspondante, ou null si non trouvée
     */
    Rating findById(Integer id);

    /**
     * Supprime une entité Rating par son identifiant.
     *
     * @param id l'identifiant de la Rating à supprimer
     */
    void delete(Integer id);
}
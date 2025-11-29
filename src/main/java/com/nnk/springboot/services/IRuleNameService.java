package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;

import java.util.List;

/**
 * Service pour gérer les opérations sur les RuleName.
 * Fournit les méthodes CRUD pour manipuler les entités RuleName.
 */
public interface IRuleNameService {

    /**
     * Récupère toutes les entités RuleName existantes.
     *
     * @return une liste de RuleName, vide si aucune n'existe
     */
    List<RuleName> findAll();

    /**
     * Enregistre une nouvelle entité RuleName ou met à jour une existante.
     *
     * @param ruleName l'entité RuleName à sauvegarder
     * @return la RuleName sauvegardée avec son ID généré si nécessaire
     */
    RuleName save(RuleName ruleName);

    /**
     * Cherche une entité RuleName par son identifiant.
     *
     * @param id l'identifiant de la RuleName
     * @return la RuleName correspondante, ou null si non trouvée
     */
    RuleName findById(Integer id);

    /**
     * Supprime une entité RuleName par son identifiant.
     *
     * @param id l'identifiant de la RuleName à supprimer
     */
    void delete(Integer id);
}
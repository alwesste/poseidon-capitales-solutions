package com.nnk.springboot.services;


import com.nnk.springboot.domain.Trade;

import java.util.List;

/**
 * Service pour gérer les opérations sur les Trades.
 * Fournit les méthodes CRUD pour manipuler les entités Trade.
 */
public interface ITradeService {

    /**
     * Récupère toutes les entités Trade existantes.
     *
     * @return une liste de Trade, vide si aucune n'existe
     */
    List<Trade> findAll();

    /**
     * Enregistre une nouvelle entité Trade ou met à jour une existante.
     *
     * @param trade l'entité Trade à sauvegarder
     * @return la Trade sauvegardée avec son ID généré si nécessaire
     */
    Trade save(Trade trade);

    /**
     * Cherche une entité Trade par son identifiant.
     *
     * @param id l'identifiant de la Trade
     * @return la Trade correspondante, ou null si non trouvée
     */
    Trade findById(Integer id);

    /**
     * Supprime une entité Trade par son identifiant.
     *
     * @param id l'identifiant de la Trade à supprimer
     */
    void delete(Integer id);
}

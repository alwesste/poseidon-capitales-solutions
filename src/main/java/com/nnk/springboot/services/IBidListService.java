package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;

import java.util.List;

/**
 * Service pour gérer les opérations sur les BidLists.
 * Définit les méthodes CRUD de base pour manipuler les entités BidList.
 */
public interface IBidListService {

    /**
     * Récupère toutes les BidList existantes.
     *
     * @return une liste de BidList, vide si aucune n'existe
     */
    List<BidList> findAll();

    /**
     * Enregistre une nouvelle BidList ou met à jour une existante.
     *
     * @param bidList l'entité BidList à sauvegarder
     * @return la BidList sauvegardée avec son ID généré si nécessaire
     */
    BidList save(BidList bidList);

    /**
     * Cherche une BidList par son identifiant.
     *
     * @param id l'identifiant de la BidList
     * @return la BidList correspondante, ou null si non trouvée
     */
    BidList findById(Integer id);

    /**
     * Supprime une BidList par son identifiant.
     *
     * @param id l'identifiant de la BidList à supprimer
     */
    void delete(Integer id);
}

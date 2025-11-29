package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;

import java.util.List;

/**
 * Service pour gérer les opérations sur les CurvePoints.
 * Définit les méthodes CRUD de base pour manipuler les entités CurvePoint.
 */
public interface ICurvePointService {

    /**
     * Récupère toutes les entités CurvePoint existantes.
     *
     * @return une liste de CurvePoint, vide si aucune n'existe
     */
    List<CurvePoint> findAll();

    /**
     * Enregistre une nouvelle CurvePoint ou met à jour une existante.
     *
     * @param curvePoint l'entité CurvePoint à sauvegarder
     * @return la CurvePoint sauvegardée avec son ID généré si nécessaire
     */
    CurvePoint save(CurvePoint curvePoint);

    /**
     * Cherche une CurvePoint par son identifiant.
     *
     * @param id l'identifiant de la CurvePoint
     * @return la CurvePoint correspondante, ou null si non trouvée
     */
    CurvePoint findById(Integer id);

    /**
     * Supprime une CurvePoint par son identifiant.
     *
     * @param id l'identifiant de la CurvePoint à supprimer
     */
    void delete(Integer id);
}
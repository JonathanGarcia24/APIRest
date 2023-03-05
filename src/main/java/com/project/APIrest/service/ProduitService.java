package com.project.APIrest.service;

import com.project.APIrest.modele.Produit;

import java.util.List;
import java.util.Optional;

public interface ProduitService {

    Produit creer(Produit produit);

    List<Produit> lire();

    Optional<Produit> lireid(Long id);

    Produit modifier(Long id, Produit produit);

    String supprimer(Long id);
}

package com.project.APIrest.repository;

import com.project.APIrest.modele.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

}

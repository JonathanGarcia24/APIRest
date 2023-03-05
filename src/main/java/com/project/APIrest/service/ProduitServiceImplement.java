package com.project.APIrest.service;

import com.project.APIrest.modele.Produit;
import com.project.APIrest.repository.ProduitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProduitServiceImplement implements ProduitService{

    private final ProduitRepository produitRepository;

    @Override
    public Produit creer(Produit produit) {
        return produitRepository.save(produit);
    }

    @Override
    public List<Produit> lire() {
        return produitRepository.findAll();
    }

    @Override
    public Optional<Produit> lireid(Long id) {
        return produitRepository.findById(id);
    }
    
    @Override
    public Produit modifier(Long id, Produit produit) {
        return produitRepository.findById(id)
                .map(p->{
                    p.setPrix(produit.getPrix());
                    p.setNom(produit.getNom());
                    p.setDescription(produit.getDescription());
                    return produitRepository.save(p);
                }).orElseThrow(()-> new RuntimeException("Produit non trouvé !"));
    }

    @Override
    public String supprimer(Long id) {
        produitRepository.deleteById(id);
        return "Produit supprimé";
    }
}

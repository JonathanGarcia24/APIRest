package com.project.APIrest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.project.APIrest.modele.Produit;
import com.project.APIrest.service.ProduitService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/produit")
@AllArgsConstructor
public class ProduitController {
    private final ProduitService produitService;

    @PostMapping("/create")
    public Produit create(@RequestBody Produit produit){
       return produitService.creer(produit);
    }

    @GetMapping("/read")
    public Map<String, Object> read(HttpServletRequest request) {
        int limitedNumber = 20;
        List<Produit> produits = produitService.lire();
        Map<String, Object> infos = new HashMap<>();

        // récupérer les paramètres de requête
        Map<String, String[]> params = request.getParameterMap();
        List<Produit> produitsFiltres = new ArrayList<>(produits);
        for (String key : params.keySet()) {
            if (!key.equals("$skip")) {
                String[] values = params.get(key);
                if (values.length == 1) {
                    String value = values[0];
                    produitsFiltres = produitsFiltres.stream().filter(p ->
                            getValue(p, key).equals(value)).collect(Collectors.toList());
                }
            }
        }

// récupérer la valeur du paramètre "skip" de la requête
        String skipParam = request.getParameter("$skip");
        int skip = 0;
        if (skipParam != null) {
            skip = Integer.parseInt(skipParam);
        }

        // appliquer le filtre skip si nécessaire
        if (skip > 0) {
            produitsFiltres = produitsFiltres.stream().skip(skip).collect(Collectors.toList());
        }

        List<Produit> limitedProduits = produitsFiltres.stream().limit(limitedNumber).collect(Collectors.toList());
        Map<String, Object> response = new TreeMap<>(Collections.reverseOrder());
        response.put("data", limitedProduits);
        infos.put("count",limitedProduits.size()+skip);
        infos.put("skip", skip);
        infos.put("limit",limitedNumber);

        response.put("infos", infos);

        return response;
    }

    private String getValue(Produit produit, String fieldName) {
        try {
            Field field = Produit.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(produit).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/read/{id}")
    public Optional<Produit> read2(@PathVariable Long id){
        return produitService.lireid(id);
    }


    @PutMapping("/update/{id}")
    public Produit update(@PathVariable Long id ,@RequestBody Produit produit){
        return produitService.modifier(id,produit);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return produitService.supprimer(id);
    }
}

package org.ms.produitservice.web;

import org.ms.produitservice.entities.Produit;
import org.ms.produitservice.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produits")
public class ProductRestController {

    @Autowired
    private ProduitRepository produitRepository;

    // Create a new Produit
    @PostMapping
    public ResponseEntity<Produit> createProduit(@RequestBody Produit produit) {
        Produit savedProduit = produitRepository.save(produit);
        return ResponseEntity.ok(savedProduit);
    }

    // Get all Produits
    @GetMapping
    public ResponseEntity<List<Produit>> getAllProduits() {
        List<Produit> produits = produitRepository.findAll();
        return ResponseEntity.ok(produits);
    }

    // Get a specific Produit by ID
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        Produit produit = produitRepository.findById(id).orElse(null);
        if (produit != null) {
            return ResponseEntity.ok(produit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update a Produit by ID
    @PutMapping("/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable Long id, @RequestBody Produit updatedProduit) {
        if (produitRepository.existsById(id)) {
            updatedProduit.setId(id);
            Produit savedProduit = produitRepository.save(updatedProduit);
            return ResponseEntity.ok(savedProduit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a Produit by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        if (produitRepository.existsById(id)) {
            produitRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

package org.ms.produitservice.web;

import org.ms.produitservice.entities.Produit;
import org.ms.produitservice.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/produits")
public class ProductRestController {
    @Autowired
    private ProduitRepository produitRepository;
    @GetMapping(path="/")
    public List<Produit> getAllProduits()
    {
        return produitRepository.findAll();
    }
    @GetMapping(path="/{id}")
    public Produit getOne( @PathVariable Long id)
    {
        return produitRepository.findById(id).get();
    }
    @PostMapping(path="/")
    public Produit save(@RequestBody Produit produit)
    {
        return produitRepository.save(produit);
    }
    @PutMapping (path="/{id}")
    public Produit update(@PathVariable Long id,@RequestBody Produit produit)
    {
        produit.setId(id);
        return produitRepository.save(produit);
    }
    @DeleteMapping (path="/{id}")
    public void delete( @PathVariable Long id)
    {
        produitRepository.deleteById( id);
    }

    @GetMapping(path = "/sorted-by-price")
    public List<Produit> getAllProduitsSortedByPrice(@RequestParam(required = false, defaultValue = "asc") String order) {
        List<Produit> produits;

        if ("desc".equalsIgnoreCase(order)) {
            // Sort by price in descending order (High to Low)
            produits = produitRepository.findAllByOrderByPriceDesc();
        } else {
            // Default to ascending order (Low to High)
            produits = produitRepository.findAllByOrderByPriceAsc();
        }

        return produits;
    }
}
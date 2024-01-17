package org.ms.factureservice.web;
import feign.Body;
import org.ms.factureservice.entities.Facture;
import org.ms.factureservice.entities.FactureLigne;
import org.ms.factureservice.feign.ClientServiceClient;
import org.ms.factureservice.feign.ProduitServiceClient;
import org.ms.factureservice.model.Client;
import org.ms.factureservice.model.Produit;
import org.ms.factureservice.repository.FactureLigneRepository;
import org.ms.factureservice.repository.FactureRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class FactureRestController {
    private FactureRepository factureRepository;
    private FactureLigneRepository factureLigneRepository;
    private ClientServiceClient clientServiceClient;
    private ProduitServiceClient produitServiceClient;
    public FactureRestController(FactureRepository factureRepository,
                                 FactureLigneRepository
                                         factureLigneRepository,
                                 ClientServiceClient clientServiceClient,
                                 ProduitServiceClient produitServiceClient) {

        this.factureRepository = factureRepository;
        this.factureLigneRepository = factureLigneRepository;
        this.clientServiceClient = clientServiceClient;
        this.produitServiceClient = produitServiceClient;
    }
    @GetMapping (path="/full-facture/{id}")
    public Facture getFacture(@PathVariable(name = "id") Long id)
    {
        Facture facture= factureRepository.findById(id).get();
        Client client = clientServiceClient.findClientById(facture.getClientID());

        facture.setClient(client);
        facture.getFacturekignes().forEach(fl-> {
            Produit product =produitServiceClient.findProductById(fl.getProduitID());
            fl.setProduit(product);
        });
        return facture;
    }
    @GetMapping(path = "/full-facture")
    public List<Facture> getAllFactures() {
        List<Facture> factures = factureRepository.findAll();

        // Enhance the factures with additional information (e.g., clients and products)
        factures.forEach(facture -> {
            Client client = clientServiceClient.findClientById(facture.getClientID());
            facture.setClient(client);

            facture.getFacturekignes().forEach(fl -> {
                Produit product = produitServiceClient.findProductById(fl.getProduitID());
                fl.setProduit(product);
            });
        });

        return factures;
    }

    @PostMapping(path = "/full-facture")
    public Facture newFacture(@RequestBody Facture factureDetails) {

        Client client = clientServiceClient.findClientById(factureDetails.getClientID());

        Facture facture= factureRepository.save(
                new Facture(null,
                        new Date(),
                        null,
                        client,
                        factureDetails.getClientID(),factureDetails.getPrice()));

        factureDetails.getFacturekignes().forEach(p->
        {
            Produit product =produitServiceClient.findProductById(p.getProduitID());
            FactureLigne factureLigne =new FactureLigne(
                    null,
                    p.getProduitID(),
                    p.getQuantity(),
                    p.getPrice(),
                    product,
                    facture);

            factureLigneRepository.save(factureLigne);
        });


        return facture;

    }
    @PutMapping(path = "/full-facture/{factureId}")
    public Facture updateFacture(@PathVariable Long factureId, @RequestBody Facture factureDetails) {
        // Check if the facture with the given ID exists
        Optional<Facture> existingFactureOptional = factureRepository.findById(factureId);

        if (existingFactureOptional.isPresent()) {
            Facture existingFacture = existingFactureOptional.get();

            // Update existing Facture details
            existingFacture.setDateFacture(new Date());
            existingFacture.setClient(clientServiceClient.findClientById(factureDetails.getClientID()));
            existingFacture.setClientID(factureDetails.getClientID());
            existingFacture.setPrice(factureDetails.getPrice());

            // Clear existing FactureLignes

            // Save updated Facture
            Facture updatedFacture = factureRepository.save(existingFacture);
            // Save FactureLignes for the updated Facture
            factureDetails.getFacturekignes().forEach(p -> {
                if (p.getId() != null) {
                    factureLigneRepository.deleteById(p.getId());

                }

                Produit product = produitServiceClient.findProductById(p.getProduitID());
                FactureLigne factureLigne = new FactureLigne(
                        null,
                        p.getProduitID(),
                        p.getQuantity(),
                        p.getPrice(),
                        product,
                        updatedFacture);

                factureLigneRepository.save(factureLigne);
            });

            return updatedFacture;
        } else {
            // Facture with the given ID not found
            throw new ResourceNotFoundException("Facture not found with id: " + factureId);
        }
    }
    @DeleteMapping (path = "/full-facture/{factureId}")
    public Facture deleteFacture(@PathVariable Long factureId) {
        // Check if the facture with the given ID exists

        Optional<Facture> existingFactureOptional =  factureRepository.findById(factureId);

        if (existingFactureOptional.isPresent()) {
            Facture facture = existingFactureOptional.get();
           Collection<FactureLigne> facturesLignes =  facture.getFacturekignes();
            facturesLignes.forEach(p -> {
               factureLigneRepository.deleteById(p.getId());
            });
            factureRepository.deleteById(factureId);
            return facture;

        }else {
            // Facture with the given ID not found
            throw new ResourceNotFoundException("Facture not found with id: " + factureId);
        }

    }
}

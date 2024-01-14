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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

    @PostMapping(path = "/full-facture")
    public Facture newFacture(@RequestBody Facture factureDetails) {

        Facture facture= factureRepository.save(
                new Facture(null,
                        new Date(),
                        null,
                        factureDetails.getClient(),
                        factureDetails.getClient().getId()));

        factureDetails.getFacturekignes().forEach(p->
        {

            FactureLigne factureLigne =new FactureLigne(
                    null,
                    p.getProduitID(),
                    p.getQuantity(),
                    p.getPrice(),
                    p.getProduit(),
                    facture);

            factureLigneRepository.save(factureLigne);
        });


        return facture;

    }
    @PutMapping(path = "/full-facture/{id}")
    public Facture updateFacture(@PathVariable(name = "id") Long id, @RequestBody Facture updatedFacture) {
        Facture existingFacture = factureRepository.findById(id).orElse(null);

        if (existingFacture != null) {
            existingFacture.setDateFacture(updatedFacture.getDateFacture());
            existingFacture.setClientID(updatedFacture.getClientID());

            // Update the facture in the database
            factureRepository.save(existingFacture);
        }

        return existingFacture;
    }

    @GetMapping(path = "/full-facture")
    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }
}

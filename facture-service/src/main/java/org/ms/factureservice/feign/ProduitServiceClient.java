package org.ms.factureservice.feign;
import org.ms.factureservice.model.Produit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="PRODUIT-SERVICE")
public interface ProduitServiceClient {
    @GetMapping(path="/produits")
    PagedModel<Produit> getAllProduits();
    @GetMapping(path="/produits/{id}")
    Produit findProductById(@PathVariable(name="id") Long id);
}
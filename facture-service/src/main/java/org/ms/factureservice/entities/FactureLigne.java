package org.ms.factureservice.entities;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.ms.factureservice.model.Produit;
import jakarta.persistence.*;
@Entity
@Data
public class FactureLigne {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long produitID;
    private long quantity;
    private double price;
    @Transient
    private Produit produit;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private Facture facture;

    public FactureLigne() {
    }


    public FactureLigne(Long id, Long produitID, long quantity, double price, Produit produit, Facture facture) {
        this.id = id;
        this.produitID = produitID;
        this.quantity = quantity;
        this.price = price;
        this.produit = produit;
        this.facture = facture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProduitID() {
        return produitID;
    }

    public void setProduitID(Long produitID) {
        this.produitID = produitID;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }
}

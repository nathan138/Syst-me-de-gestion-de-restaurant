package fr.epf.restaurant.dto;

import java.math.BigDecimal;

public class PrixIngredientDto {
    private long fournisseurId;
    private String fournisseurNom;
    private BigDecimal prixUnitaire;

    public PrixIngredientDto() {
    }

    public PrixIngredientDto(long fournisseurId, String fournisseurNom, BigDecimal prixUnitaire) {
        this.fournisseurId = fournisseurId;
        this.fournisseurNom = fournisseurNom;
        this.prixUnitaire = prixUnitaire;
    }

    public long getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(long fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public String getFournisseurNom() {
        return fournisseurNom;
    }

    public void setFournisseurNom(String fournisseurNom) {
        this.fournisseurNom = fournisseurNom;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
}

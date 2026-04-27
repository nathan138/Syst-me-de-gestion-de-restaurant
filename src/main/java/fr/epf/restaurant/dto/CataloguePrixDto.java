package fr.epf.restaurant.dto;

import java.math.BigDecimal;

public class CataloguePrixDto {
    private long ingredientId;
    private String ingredientNom;
    private String ingredientUnite;
    private BigDecimal prixUnitaire;

    public CataloguePrixDto() {
    }

    public CataloguePrixDto(long ingredientId, String ingredientNom, String ingredientUnite, BigDecimal prixUnitaire) {
        this.ingredientId = ingredientId;
        this.ingredientNom = ingredientNom;
        this.ingredientUnite = ingredientUnite;
        this.prixUnitaire = prixUnitaire;
    }

    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientNom() {
        return ingredientNom;
    }

    public void setIngredientNom(String ingredientNom) {
        this.ingredientNom = ingredientNom;
    }

    public String getIngredientUnite() {
        return ingredientUnite;
    }

    public void setIngredientUnite(String ingredientUnite) {
        this.ingredientUnite = ingredientUnite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
}

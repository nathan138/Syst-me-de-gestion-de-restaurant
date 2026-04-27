package fr.epf.restaurant.controller.model;

import java.math.BigDecimal;

public class LigneCommandeFournisseur {
    private long id;
    private long commandeFournisseurId;
    private Ingredient ingredient;
    private double quantiteCommandee;
    private BigDecimal prixUnitaire;

    // Constructeur vide
    public LigneCommandeFournisseur() {}

    // Constructeur complet
    public LigneCommandeFournisseur(long id, long commandeFournisseurId, Ingredient ingredient,
                                    double quantiteCommandee, BigDecimal prixUnitaire) {
        this.id = id;
        this.commandeFournisseurId = commandeFournisseurId;
        this.ingredient = ingredient;
        this.quantiteCommandee = quantiteCommandee;
        this.prixUnitaire = prixUnitaire;
    }

    // Constructeur sans id (pour creation)
    public LigneCommandeFournisseur(long commandeFournisseurId, Ingredient ingredient,
                                    double quantiteCommandee, BigDecimal prixUnitaire) {
        this.commandeFournisseurId = commandeFournisseurId;
        this.ingredient = ingredient;
        this.quantiteCommandee = quantiteCommandee;
        this.prixUnitaire = prixUnitaire;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCommandeFournisseurId() {
        return commandeFournisseurId;
    }

    public void setCommandeFournisseurId(long commandeFournisseurId) {
        this.commandeFournisseurId = commandeFournisseurId;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getQuantiteCommandee() {
        return quantiteCommandee;
    }

    public void setQuantiteCommandee(double quantiteCommandee) {
        this.quantiteCommandee = quantiteCommandee;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    @Override
    public String toString() {
        return "LigneCommandeFournisseur{" +
                "id=" + id +
                ", commandeFournisseurId=" + commandeFournisseurId +
                ", ingredient=" + ingredient +
                ", quantiteCommandee=" + quantiteCommandee +
                ", prixUnitaire=" + prixUnitaire +
                '}';
    }
}

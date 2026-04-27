package fr.epf.restaurant.dto;

import fr.epf.restaurant.controller.model.Ingredient;

public class AlerteStockDto {
    private Ingredient ingredient;
    private double stockActuel;
    private double seuilAlerte;
    private double quantiteCommandee;
    private Long commandeFournisseurId;

    public AlerteStockDto() {
    }

    public AlerteStockDto(
            Ingredient ingredient,
            double stockActuel,
            double seuilAlerte,
            double quantiteCommandee,
            Long commandeFournisseurId
    ) {
        this.ingredient = ingredient;
        this.stockActuel = stockActuel;
        this.seuilAlerte = seuilAlerte;
        this.quantiteCommandee = quantiteCommandee;
        this.commandeFournisseurId = commandeFournisseurId;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getStockActuel() {
        return stockActuel;
    }

    public void setStockActuel(double stockActuel) {
        this.stockActuel = stockActuel;
    }

    public double getSeuilAlerte() {
        return seuilAlerte;
    }

    public void setSeuilAlerte(double seuilAlerte) {
        this.seuilAlerte = seuilAlerte;
    }

    public double getQuantiteCommandee() {
        return quantiteCommandee;
    }

    public void setQuantiteCommandee(double quantiteCommandee) {
        this.quantiteCommandee = quantiteCommandee;
    }

    public Long getCommandeFournisseurId() {
        return commandeFournisseurId;
    }

    public void setCommandeFournisseurId(Long commandeFournisseurId) {
        this.commandeFournisseurId = commandeFournisseurId;
    }
}

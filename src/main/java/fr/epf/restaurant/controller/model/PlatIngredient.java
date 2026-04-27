package fr.epf.restaurant.controller.model;

public class PlatIngredient {
    private Ingredient ingredient;
    private double quantiteRequise;

    // Constructeur vide
    public PlatIngredient() {}

    // Constructeur complet
    public PlatIngredient(Ingredient ingredient, double quantiteRequise) {
        this.ingredient = ingredient;
        this.quantiteRequise = quantiteRequise;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getQuantiteRequise() {
        return quantiteRequise;
    }

    public void setQuantiteRequise(double quantiteRequise) {
        this.quantiteRequise = quantiteRequise;
    }

    @Override
    public String toString() {
        return "PlatIngredient{" +
                "ingredient=" + ingredient +
                ", quantiteRequise=" + quantiteRequise +
                '}';
    }
}

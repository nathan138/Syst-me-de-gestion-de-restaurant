package fr.epf.restaurant.controller.model;

import java.math.BigDecimal;
import java.util.List;

public class Plat {
    private long id;
    private String nom;
    private String description;
    private BigDecimal prix;
    private List<PlatIngredient> ingredients;

    // Constructeur vide
    public Plat() {}

    // Constructeur complet
    public Plat(long id, String nom, String description, BigDecimal prix) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
    }

    // Constructeur sans id (pour creation)
    public Plat(String nom, String description, BigDecimal prix) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public List<PlatIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<PlatIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Plat{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                '}';
    }
}

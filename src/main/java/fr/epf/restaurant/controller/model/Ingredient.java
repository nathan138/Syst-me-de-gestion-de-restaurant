package fr.epf.restaurant.controller.model;

public class Ingredient {
    private long id;
    private String nom;
    private String unite;
    private double stockActuel;
    private double seuilAlerte;

    // Constructeur vide
    public Ingredient() {}

    // Constructeur complet
    public Ingredient(long id, String nom, String unite, double stockActuel, double seuilAlerte) {
        this.id = id;
        this.nom = nom;
        this.unite = unite;
        this.stockActuel = stockActuel;
        this.seuilAlerte = seuilAlerte;
    }

    // Constructeur sans id (pour creation)
    public Ingredient(String nom, String unite, double stockActuel, double seuilAlerte) {
        this.nom = nom;
        this.unite = unite;
        this.stockActuel = stockActuel;
        this.seuilAlerte = seuilAlerte;
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

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
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

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", unite='" + unite + '\'' +
                ", stockActuel=" + stockActuel +
                ", seuilAlerte=" + seuilAlerte +
                '}';
    }
}

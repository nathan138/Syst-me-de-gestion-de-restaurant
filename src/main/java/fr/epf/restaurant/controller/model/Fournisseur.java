package fr.epf.restaurant.controller.model;

public class Fournisseur {
    private long id;
    private String nom;
    private String contact;
    private String email;

    // Constructeur vide
    public Fournisseur() {}

    // Constructeur complet
    public Fournisseur(long id, String nom, String contact, String email) {
        this.id = id;
        this.nom = nom;
        this.contact = contact;
        this.email = email;
    }

    // Constructeur sans id (pour creation)
    public Fournisseur(String nom, String contact, String email) {
        this.nom = nom;
        this.contact = contact;
        this.email = email;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Fournisseur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

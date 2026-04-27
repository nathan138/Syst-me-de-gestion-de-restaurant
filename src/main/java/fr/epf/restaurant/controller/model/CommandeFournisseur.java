package fr.epf.restaurant.controller.model;

import java.time.LocalDateTime;
import java.util.List;

public class CommandeFournisseur {
    private long id;
    private Fournisseur fournisseur;
    private LocalDateTime dateCommande;
    private String statut;
    private List<LigneCommandeFournisseur> lignes;

    // Constructeur vide
    public CommandeFournisseur() {}

    // Constructeur complet
    public CommandeFournisseur(long id, Fournisseur fournisseur, LocalDateTime dateCommande, String statut,
                               List<LigneCommandeFournisseur> lignes) {
        this.id = id;
        this.fournisseur = fournisseur;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.lignes = lignes;
    }

    // Constructeur sans id (pour creation)
    public CommandeFournisseur(Fournisseur fournisseur, LocalDateTime dateCommande, String statut,
                               List<LigneCommandeFournisseur> lignes) {
        this.fournisseur = fournisseur;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.lignes = lignes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public List<LigneCommandeFournisseur> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneCommandeFournisseur> lignes) {
        this.lignes = lignes;
    }

    @Override
    public String toString() {
        return "CommandeFournisseur{" +
                "id=" + id +
                ", fournisseur=" + fournisseur +
                ", dateCommande=" + dateCommande +
                ", statut='" + statut + '\'' +
                ", lignes=" + lignes +
                '}';
    }
}

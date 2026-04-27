package fr.epf.restaurant.controller.model;

import java.time.LocalDateTime;
import java.util.List;

public class CommandeClient {
    private long id;
    private Client client;
    private LocalDateTime dateCommande;
    private String statut;
    private List<LigneCommandeClient> lignes;

    // Constructeur vide
    public CommandeClient() {}

    // Constructeur complet
    public CommandeClient(long id, Client client, LocalDateTime dateCommande, String statut,
                          List<LigneCommandeClient> lignes) {
        this.id = id;
        this.client = client;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.lignes = lignes;
    }

    // Constructeur sans id (pour creation)
    public CommandeClient(Client client, LocalDateTime dateCommande, String statut,
                          List<LigneCommandeClient> lignes) {
        this.client = client;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public List<LigneCommandeClient> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneCommandeClient> lignes) {
        this.lignes = lignes;
    }

    @Override
    public String toString() {
        return "CommandeClient{" +
                "id=" + id +
                ", client=" + client +
                ", dateCommande=" + dateCommande +
                ", statut='" + statut + '\'' +
                ", lignes=" + lignes +
                '}';
    }
}

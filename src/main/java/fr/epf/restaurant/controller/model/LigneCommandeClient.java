package fr.epf.restaurant.controller.model;

public class LigneCommandeClient {
    private long id;
    private long commandeClientId;
    private Plat plat;
    private int quantite;

    // Constructeur vide
    public LigneCommandeClient() {}

    // Constructeur complet
    public LigneCommandeClient(long id, long commandeClientId, Plat plat, int quantite) {
        this.id = id;
        this.commandeClientId = commandeClientId;
        this.plat = plat;
        this.quantite = quantite;
    }

    // Constructeur sans id (pour creation)
    public LigneCommandeClient(long commandeClientId, Plat plat, int quantite) {
        this.commandeClientId = commandeClientId;
        this.plat = plat;
        this.quantite = quantite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCommandeClientId() {
        return commandeClientId;
    }

    public void setCommandeClientId(long commandeClientId) {
        this.commandeClientId = commandeClientId;
    }

    public Plat getPlat() {
        return plat;
    }

    public void setPlat(Plat plat) {
        this.plat = plat;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        return "LigneCommandeClient{" +
                "id=" + id +
                ", commandeClientId=" + commandeClientId +
                ", plat=" + plat +
                ", quantite=" + quantite +
                '}';
    }
}

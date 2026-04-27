package fr.epf.restaurant.dto;

import java.util.List;

public class CreerCommandeClientRequest {
    private long clientId;
    private List<LigneCommandeClientRequest> lignes;

    public CreerCommandeClientRequest() {
    }

    public CreerCommandeClientRequest(long clientId, List<LigneCommandeClientRequest> lignes) {
        this.clientId = clientId;
        this.lignes = lignes;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public List<LigneCommandeClientRequest> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneCommandeClientRequest> lignes) {
        this.lignes = lignes;
    }

    public static class LigneCommandeClientRequest {
        private long platId;
        private int quantite;

        public LigneCommandeClientRequest() {
        }

        public LigneCommandeClientRequest(long platId, int quantite) {
            this.platId = platId;
            this.quantite = quantite;
        }

        public long getPlatId() {
            return platId;
        }

        public void setPlatId(long platId) {
            this.platId = platId;
        }

        public int getQuantite() {
            return quantite;
        }

        public void setQuantite(int quantite) {
            this.quantite = quantite;
        }
    }
}

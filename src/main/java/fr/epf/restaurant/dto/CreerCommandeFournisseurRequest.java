package fr.epf.restaurant.dto;

import java.math.BigDecimal;
import java.util.List;

public class CreerCommandeFournisseurRequest {
    private long fournisseurId;
    private List<LigneCommandeFournisseurRequest> lignes;

    public CreerCommandeFournisseurRequest() {
    }

    public CreerCommandeFournisseurRequest(
            long fournisseurId,
            List<LigneCommandeFournisseurRequest> lignes
    ) {
        this.fournisseurId = fournisseurId;
        this.lignes = lignes;
    }

    public long getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(long fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public List<LigneCommandeFournisseurRequest> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneCommandeFournisseurRequest> lignes) {
        this.lignes = lignes;
    }

    public static class LigneCommandeFournisseurRequest {
        private long ingredientId;
        private double quantite;
        private BigDecimal prixUnitaire;

        public LigneCommandeFournisseurRequest() {
        }

        public LigneCommandeFournisseurRequest(long ingredientId, double quantite, BigDecimal prixUnitaire) {
            this.ingredientId = ingredientId;
            this.quantite = quantite;
            this.prixUnitaire = prixUnitaire;
        }

        public long getIngredientId() {
            return ingredientId;
        }

        public void setIngredientId(long ingredientId) {
            this.ingredientId = ingredientId;
        }

        public double getQuantite() {
            return quantite;
        }

        public void setQuantite(double quantite) {
            this.quantite = quantite;
        }

        public BigDecimal getPrixUnitaire() {
            return prixUnitaire;
        }

        public void setPrixUnitaire(BigDecimal prixUnitaire) {
            this.prixUnitaire = prixUnitaire;
        }
    }
}

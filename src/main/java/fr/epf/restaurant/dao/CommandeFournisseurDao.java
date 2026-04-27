package fr.epf.restaurant.dao;

import fr.epf.restaurant.controller.model.CommandeFournisseur;
import fr.epf.restaurant.controller.model.Fournisseur;
import fr.epf.restaurant.controller.model.Ingredient;
import fr.epf.restaurant.controller.model.LigneCommandeFournisseur;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CommandeFournisseurDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertCommandeFournisseur;

    private final RowMapper<CommandeFournisseur> commandeMapper = (rs, rowNum) -> {
        CommandeFournisseur commande = new CommandeFournisseur();
        commande.setId(rs.getLong("id"));
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(rs.getLong("fournisseur_id"));
        commande.setFournisseur(fournisseur);
        commande.setDateCommande(rs.getTimestamp("date_commande").toLocalDateTime());
        commande.setStatut(rs.getString("statut"));
        return commande;
    };

    public CommandeFournisseurDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertCommandeFournisseur = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("COMMANDE_FOURNISSEUR")
                .usingGeneratedKeyColumns("id");
    }

    public List<CommandeFournisseur> findAll(String statut) {
        List<CommandeFournisseur> commandes;
        if (statut == null || statut.isBlank()) {
            commandes = jdbcTemplate.query(
                    "SELECT id, fournisseur_id, date_commande, statut FROM COMMANDE_FOURNISSEUR ORDER BY id DESC",
                    commandeMapper
            );
        } else {
            commandes = jdbcTemplate.query(
                    "SELECT id, fournisseur_id, date_commande, statut FROM COMMANDE_FOURNISSEUR "
                            + "WHERE statut = ? ORDER BY id DESC",
                    commandeMapper,
                    statut
            );
        }
        for (CommandeFournisseur commande : commandes) {
            commande.setLignes(findLignesByCommandeId(commande.getId()));
        }
        return commandes;
    }

    public Optional<CommandeFournisseur> findById(long id) {
        List<CommandeFournisseur> commandes = jdbcTemplate.query(
                "SELECT id, fournisseur_id, date_commande, statut FROM COMMANDE_FOURNISSEUR WHERE id = ?",
                commandeMapper,
                id
        );
        if (commandes.isEmpty()) {
            return Optional.empty();
        }
        CommandeFournisseur commande = commandes.get(0);
        commande.setLignes(findLignesByCommandeId(id));
        return Optional.of(commande);
    }

    public long insert(long fournisseurId, LocalDateTime dateCommande, String statut) {
        Map<String, Object> params = new HashMap<>();
        params.put("fournisseur_id", fournisseurId);
        params.put("date_commande", java.sql.Timestamp.valueOf(dateCommande));
        params.put("statut", statut);
        Number key = insertCommandeFournisseur.executeAndReturnKey(params);
        return key == null ? -1L : key.longValue();
    }

    public void insertLigne(long commandeId, long ingredientId, double quantite, java.math.BigDecimal prixUnitaire) {
        jdbcTemplate.update(
                "INSERT INTO LIGNE_COMMANDE_FOURNISSEUR(commande_fournisseur_id, ingredient_id, "
                        + "quantite_commandee, prix_unitaire) VALUES (?, ?, ?, ?)",
                commandeId,
                ingredientId,
                quantite,
                prixUnitaire
        );
    }

    public int updateStatut(long id, String statut) {
        return jdbcTemplate.update("UPDATE COMMANDE_FOURNISSEUR SET statut = ? WHERE id = ?", statut, id);
    }

    public int delete(long id) {
        jdbcTemplate.update("DELETE FROM LIGNE_COMMANDE_FOURNISSEUR WHERE commande_fournisseur_id = ?", id);
        return jdbcTemplate.update("DELETE FROM COMMANDE_FOURNISSEUR WHERE id = ?", id);
    }

    public List<LigneCommandeFournisseur> findLignesByCommandeId(long commandeId) {
        return jdbcTemplate.query("""
                SELECT l.id, l.commande_fournisseur_id, l.quantite_commandee, l.prix_unitaire,
                       i.id AS ingredient_id, i.nom, i.unite, i.stock_actuel, i.seuil_alerte
                FROM LIGNE_COMMANDE_FOURNISSEUR l
                JOIN INGREDIENT i ON i.id = l.ingredient_id
                WHERE l.commande_fournisseur_id = ?
                ORDER BY l.id
                """, (rs, rowNum) -> {
            LigneCommandeFournisseur ligne = new LigneCommandeFournisseur();
            ligne.setId(rs.getLong("id"));
            ligne.setCommandeFournisseurId(rs.getLong("commande_fournisseur_id"));
            Ingredient ingredient = new Ingredient(
                    rs.getLong("ingredient_id"),
                    rs.getString("nom"),
                    rs.getString("unite"),
                    rs.getDouble("stock_actuel"),
                    rs.getDouble("seuil_alerte")
            );
            ligne.setIngredient(ingredient);
            ligne.setQuantiteCommandee(rs.getDouble("quantite_commandee"));
            ligne.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
            return ligne;
        }, commandeId);
    }
}

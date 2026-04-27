package fr.epf.restaurant.dao;

import fr.epf.restaurant.controller.model.Client;
import fr.epf.restaurant.controller.model.CommandeClient;
import fr.epf.restaurant.controller.model.LigneCommandeClient;
import fr.epf.restaurant.controller.model.Plat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CommandeClientDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertCommandeClient;

    private final RowMapper<CommandeClient> commandeMapper = (rs, rowNum) -> {
        CommandeClient commande = new CommandeClient();
        commande.setId(rs.getLong("id"));
        Client client = new Client();
        client.setId(rs.getLong("client_id"));
        commande.setClient(client);
        commande.setDateCommande(rs.getTimestamp("date_commande").toLocalDateTime());
        commande.setStatut(rs.getString("statut"));
        return commande;
    };

    public CommandeClientDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertCommandeClient = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("COMMANDE_CLIENT")
                .usingGeneratedKeyColumns("id");
    }

    public List<CommandeClient> findAll(String statut) {
        List<CommandeClient> commandes;
        if (statut == null || statut.isBlank()) {
            commandes = jdbcTemplate.query(
                    "SELECT id, client_id, date_commande, statut FROM COMMANDE_CLIENT ORDER BY id DESC",
                    commandeMapper
            );
        } else {
            commandes = jdbcTemplate.query(
                    "SELECT id, client_id, date_commande, statut "
                            + "FROM COMMANDE_CLIENT WHERE statut = ? ORDER BY id DESC",
                    commandeMapper,
                    statut
            );
        }
        for (CommandeClient commande : commandes) {
            commande.setLignes(findLignesByCommandeId(commande.getId()));
        }
        return commandes;
    }

    public Optional<CommandeClient> findById(long id) {
        List<CommandeClient> commandes = jdbcTemplate.query(
                "SELECT id, client_id, date_commande, statut FROM COMMANDE_CLIENT WHERE id = ?",
                commandeMapper,
                id
        );
        if (commandes.isEmpty()) {
            return Optional.empty();
        }
        CommandeClient commande = commandes.get(0);
        commande.setLignes(findLignesByCommandeId(id));
        return Optional.of(commande);
    }

    public long insert(long clientId, LocalDateTime dateCommande, String statut) {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("date_commande", java.sql.Timestamp.valueOf(dateCommande));
        params.put("statut", statut);
        Number key = insertCommandeClient.executeAndReturnKey(params);
        return key == null ? -1L : key.longValue();
    }

    public void insertLigne(long commandeId, long platId, int quantite) {
        jdbcTemplate.update(
                "INSERT INTO LIGNE_COMMANDE_CLIENT(commande_client_id, plat_id, quantite) VALUES (?, ?, ?)",
                commandeId,
                platId,
                quantite
        );
    }

    public int updateStatut(long id, String statut) {
        return jdbcTemplate.update("UPDATE COMMANDE_CLIENT SET statut = ? WHERE id = ?", statut, id);
    }

    public int delete(long id) {
        jdbcTemplate.update("DELETE FROM LIGNE_COMMANDE_CLIENT WHERE commande_client_id = ?", id);
        return jdbcTemplate.update("DELETE FROM COMMANDE_CLIENT WHERE id = ?", id);
    }

    public List<LigneCommandeClient> findLignesByCommandeId(long commandeId) {
        return jdbcTemplate.query("""
                SELECT l.id, l.commande_client_id, l.quantite, p.id AS plat_id, p.nom, p.description, p.prix
                FROM LIGNE_COMMANDE_CLIENT l
                JOIN PLAT p ON p.id = l.plat_id
                WHERE l.commande_client_id = ?
                ORDER BY l.id
                """, (rs, rowNum) -> {
            LigneCommandeClient ligne = new LigneCommandeClient();
            ligne.setId(rs.getLong("id"));
            ligne.setCommandeClientId(rs.getLong("commande_client_id"));
            Plat plat = new Plat(
                    rs.getLong("plat_id"),
                    rs.getString("nom"),
                    rs.getString("description"),
                    rs.getBigDecimal("prix")
            );
            ligne.setPlat(plat);
            ligne.setQuantite(rs.getInt("quantite"));
            return ligne;
        }, commandeId);
    }

    public List<LignePlatQuantite> findIngredientNeeds(long commandeId) {
        List<LigneCommandeClient> lignes = findLignesByCommandeId(commandeId);
        List<LignePlatQuantite> out = new ArrayList<>();
        for (LigneCommandeClient ligne : lignes) {
            out.add(new LignePlatQuantite(ligne.getPlat().getId(), ligne.getQuantite()));
        }
        return out;
    }

    public static class LignePlatQuantite {
        private final long platId;
        private final int quantite;

        public LignePlatQuantite(long platId, int quantite) {
            this.platId = platId;
            this.quantite = quantite;
        }

        public long getPlatId() {
            return platId;
        }

        public int getQuantite() {
            return quantite;
        }
    }
}

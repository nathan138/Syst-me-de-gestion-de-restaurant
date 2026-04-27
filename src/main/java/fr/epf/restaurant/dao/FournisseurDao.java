package fr.epf.restaurant.dao;

import fr.epf.restaurant.controller.model.Fournisseur;
import fr.epf.restaurant.dto.CataloguePrixDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class FournisseurDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertFournisseur;

    private final RowMapper<Fournisseur> mapper = (rs, rowNum) -> new Fournisseur(
            rs.getLong("id"),
            rs.getString("nom"),
            rs.getString("contact"),
            rs.getString("email")
    );

    public FournisseurDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertFournisseur = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FOURNISSEUR")
                .usingGeneratedKeyColumns("id");
    }

    public List<Fournisseur> findAll() {
        return jdbcTemplate.query("SELECT id, nom, contact, email FROM FOURNISSEUR ORDER BY id", mapper);
    }

    public Optional<Fournisseur> findById(long id) {
        List<Fournisseur> fournisseurs = jdbcTemplate.query(
                "SELECT id, nom, contact, email FROM FOURNISSEUR WHERE id = ?",
                mapper,
                id
        );
        return fournisseurs.stream().findFirst();
    }

    public boolean existsById(long id) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FOURNISSEUR WHERE id = ?", Integer.class, id);
        return count != null && count > 0;
    }

    public Fournisseur insert(Fournisseur fournisseur) {
        Map<String, Object> params = new HashMap<>();
        params.put("nom", fournisseur.getNom());
        params.put("contact", fournisseur.getContact());
        params.put("email", fournisseur.getEmail());
        Number key = insertFournisseur.executeAndReturnKey(params);
        return findById(key.longValue()).orElseThrow();
    }

    public List<CataloguePrixDto> findCatalogueByFournisseur(long fournisseurId) {
        return jdbcTemplate.query("""
                SELECT i.id AS ingredient_id, i.nom AS ingredient_nom, i.unite AS ingredient_unite, fi.prix_unitaire
                FROM FOURNISSEUR_INGREDIENT fi
                JOIN INGREDIENT i ON i.id = fi.ingredient_id
                WHERE fi.fournisseur_id = ?
                ORDER BY i.id
                """, (rs, rowNum) -> new CataloguePrixDto(
                rs.getLong("ingredient_id"),
                rs.getString("ingredient_nom"),
                rs.getString("ingredient_unite"),
                rs.getBigDecimal("prix_unitaire")
        ), fournisseurId);
    }
}

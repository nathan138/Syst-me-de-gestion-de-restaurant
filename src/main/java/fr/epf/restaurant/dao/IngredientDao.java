package fr.epf.restaurant.dao;

import fr.epf.restaurant.controller.model.Ingredient;
import fr.epf.restaurant.dto.PrixIngredientDto;
import fr.epf.restaurant.dto.RecommandationDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class IngredientDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Ingredient> mapper = (rs, rowNum) -> new Ingredient(
            rs.getLong("id"),
            rs.getString("nom"),
            rs.getString("unite"),
            rs.getDouble("stock_actuel"),
            rs.getDouble("seuil_alerte")
    );

    public IngredientDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Ingredient> findAll() {
        return jdbcTemplate.query(
                "SELECT id, nom, unite, stock_actuel, seuil_alerte FROM INGREDIENT ORDER BY id",
                mapper
        );
    }

    public Optional<Ingredient> findById(long id) {
        List<Ingredient> ingredients = jdbcTemplate.query(
                "SELECT id, nom, unite, stock_actuel, seuil_alerte FROM INGREDIENT WHERE id = ?",
                mapper,
                id
        );
        return ingredients.stream().findFirst();
    }

    public List<Ingredient> findAlertes() {
        return jdbcTemplate.query(
                "SELECT id, nom, unite, stock_actuel, seuil_alerte "
                        + "FROM INGREDIENT WHERE stock_actuel < seuil_alerte ORDER BY id",
                mapper
        );
    }

    public int updateStock(long ingredientId, double newStock) {
        return jdbcTemplate.update("UPDATE INGREDIENT SET stock_actuel = ? WHERE id = ?", newStock, ingredientId);
    }

    public List<PrixIngredientDto> findPrixByIngredient(long ingredientId) {
        return jdbcTemplate.query("""
                SELECT f.id AS fournisseur_id, f.nom AS fournisseur_nom, fi.prix_unitaire
                FROM FOURNISSEUR_INGREDIENT fi
                JOIN FOURNISSEUR f ON f.id = fi.fournisseur_id
                WHERE fi.ingredient_id = ?
                ORDER BY fi.prix_unitaire ASC, f.id ASC
                """, (rs, rowNum) -> new PrixIngredientDto(
                rs.getLong("fournisseur_id"),
                rs.getString("fournisseur_nom"),
                rs.getBigDecimal("prix_unitaire")
        ), ingredientId);
    }

    public Optional<RecommandationDto> findBestPriceRecommendation(long ingredientId, double quantiteRecommandee) {
        List<RecommandationDto> rows = jdbcTemplate.query("""
                SELECT f.id AS fournisseur_id, f.nom AS fournisseur_nom, fi.prix_unitaire
                FROM FOURNISSEUR_INGREDIENT fi
                JOIN FOURNISSEUR f ON f.id = fi.fournisseur_id
                WHERE fi.ingredient_id = ?
                ORDER BY fi.prix_unitaire ASC, f.id ASC
                LIMIT 1
                """, (rs, rowNum) -> new RecommandationDto(
                rs.getLong("fournisseur_id"),
                rs.getString("fournisseur_nom"),
                rs.getBigDecimal("prix_unitaire"),
                quantiteRecommandee
        ), ingredientId);
        return rows.stream().findFirst();
    }
}

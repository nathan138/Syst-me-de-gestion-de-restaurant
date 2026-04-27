package fr.epf.restaurant.dao;

import fr.epf.restaurant.controller.model.Ingredient;
import fr.epf.restaurant.controller.model.Plat;
import fr.epf.restaurant.controller.model.PlatIngredient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PlatDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertPlat;

    private final RowMapper<Plat> platMapper = (rs, rowNum) -> new Plat(
            rs.getLong("id"),
            rs.getString("nom"),
            rs.getString("description"),
            rs.getBigDecimal("prix")
    );

    public PlatDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertPlat = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("PLAT")
                .usingGeneratedKeyColumns("id");
    }

    public List<Plat> findAll() {
        return jdbcTemplate.query("SELECT id, nom, description, prix FROM PLAT ORDER BY id", platMapper);
    }

    public Optional<Plat> findById(long id) {
        List<Plat> plats = jdbcTemplate.query(
                "SELECT id, nom, description, prix FROM PLAT WHERE id = ?",
                platMapper,
                id
        );
        return plats.stream().findFirst();
    }

    public boolean existsById(long id) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM PLAT WHERE id = ?", Integer.class, id);
        return count != null && count > 0;
    }

    public Plat insert(Plat plat) {
        Map<String, Object> params = new HashMap<>();
        params.put("nom", plat.getNom());
        params.put("description", plat.getDescription());
        params.put("prix", plat.getPrix());
        Number key = insertPlat.executeAndReturnKey(params);
        return findById(key.longValue()).orElseThrow();
    }

    public List<PlatIngredient> findIngredientsForPlat(long platId) {
        return jdbcTemplate.query("""
                SELECT i.id, i.nom, i.unite, i.stock_actuel, i.seuil_alerte, pi.quantite_requise
                FROM PLAT_INGREDIENT pi
                JOIN INGREDIENT i ON i.id = pi.ingredient_id
                WHERE pi.plat_id = ?
                ORDER BY i.id
                """, (rs, rowNum) -> {
            Ingredient ingredient = new Ingredient(
                    rs.getLong("id"),
                    rs.getString("nom"),
                    rs.getString("unite"),
                    rs.getDouble("stock_actuel"),
                    rs.getDouble("seuil_alerte")
            );
            return new PlatIngredient(ingredient, rs.getDouble("quantite_requise"));
        }, platId);
    }
}

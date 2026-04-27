package fr.epf.restaurant.dao;

import fr.epf.restaurant.controller.model.Client;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ClientDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertClient;

    private final RowMapper<Client> mapper = (rs, rowNum) -> new Client(
            rs.getLong("id"),
            rs.getString("nom"),
            rs.getString("prenom"),
            rs.getString("email"),
            rs.getString("telephone")
    );

    public ClientDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertClient = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("CLIENT")
                .usingGeneratedKeyColumns("id");
    }

    public List<Client> findAll() {
        return jdbcTemplate.query("SELECT id, nom, prenom, email, telephone FROM CLIENT ORDER BY id", mapper);
    }

    public Optional<Client> findById(long id) {
        List<Client> clients = jdbcTemplate.query(
                "SELECT id, nom, prenom, email, telephone FROM CLIENT WHERE id = ?",
                mapper,
                id
        );
        return clients.stream().findFirst();
    }

    public boolean existsById(long id) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM CLIENT WHERE id = ?", Integer.class, id);
        return count != null && count > 0;
    }

    public Client insert(Client client) {
        Map<String, Object> params = new HashMap<>();
        params.put("nom", client.getNom());
        params.put("prenom", client.getPrenom());
        params.put("email", client.getEmail());
        params.put("telephone", client.getTelephone());
        Number key = insertClient.executeAndReturnKey(params);
        return findById(key.longValue()).orElseThrow();
    }
}

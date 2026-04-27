package fr.epf.restaurant.service;

import fr.epf.restaurant.controller.model.Ingredient;
import fr.epf.restaurant.controller.model.PlatIngredient;
import fr.epf.restaurant.dao.IngredientDao;
import fr.epf.restaurant.dao.PlatDao;
import fr.epf.restaurant.exception.StockInsuffisantException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockService {
    private final IngredientDao ingredientDao;
    private final PlatDao platDao;

    public StockService(IngredientDao ingredientDao, PlatDao platDao) {
        this.ingredientDao = ingredientDao;
        this.platDao = platDao;
    }

    public void verifierEtConsommer(Map<Long, Integer> platQuantites) {
        Map<Long, Double> besoins = calculerBesoins(platQuantites);
        List<String> manquants = besoins.entrySet().stream()
                .map(entry -> ingredientDao.findById(entry.getKey()).orElse(null))
                .filter(ingredient -> ingredient != null)
                .filter(ingredient -> ingredient.getStockActuel() < besoins.get(ingredient.getId()))
                .map(Ingredient::getNom)
                .collect(Collectors.toList());

        if (!manquants.isEmpty()) {
            throw new StockInsuffisantException("Stock insuffisant pour : " + String.join(" | ", manquants));
        }

        for (Map.Entry<Long, Double> entry : besoins.entrySet()) {
            Ingredient ingredient = ingredientDao.findById(entry.getKey()).orElseThrow();
            ingredientDao.updateStock(ingredient.getId(), ingredient.getStockActuel() - entry.getValue());
        }
    }

    public void reapprovisionner(Map<Long, Double> ingredientQuantites) {
        for (Map.Entry<Long, Double> entry : ingredientQuantites.entrySet()) {
            Ingredient ingredient = ingredientDao.findById(entry.getKey()).orElseThrow();
            ingredientDao.updateStock(ingredient.getId(), ingredient.getStockActuel() + entry.getValue());
        }
    }

    private Map<Long, Double> calculerBesoins(Map<Long, Integer> platQuantites) {
        Map<Long, Double> besoins = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> platEntry : platQuantites.entrySet()) {
            List<PlatIngredient> ingredients = platDao.findIngredientsForPlat(platEntry.getKey());
            for (PlatIngredient pi : ingredients) {
                double besoin = pi.getQuantiteRequise() * platEntry.getValue();
                besoins.merge(pi.getIngredient().getId(), besoin, Double::sum);
            }
        }
        return besoins;
    }
}

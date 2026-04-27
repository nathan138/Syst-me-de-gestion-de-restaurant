package fr.epf.restaurant.service;

import fr.epf.restaurant.controller.model.Ingredient;
import fr.epf.restaurant.dao.IngredientDao;
import fr.epf.restaurant.dto.AlerteStockDto;
import fr.epf.restaurant.dto.PrixIngredientDto;
import fr.epf.restaurant.dto.RecommandationDto;
import fr.epf.restaurant.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    private final IngredientDao ingredientDao;

    public IngredientService(IngredientDao ingredientDao) {
        this.ingredientDao = ingredientDao;
    }

    public List<Ingredient> listerIngredients() {
        return ingredientDao.findAll();
    }

    public Ingredient getById(long id) {
        return ingredientDao.findById(id).orElseThrow(() -> new NotFoundException("Ingredient introuvable: " + id));
    }

    public List<Ingredient> getAlertes() {
        return ingredientDao.findAlertes();
    }

    public List<PrixIngredientDto> getPrix(long ingredientId) {
        getById(ingredientId);
        return ingredientDao.findPrixByIngredient(ingredientId);
    }

    public RecommandationDto getRecommandation(long ingredientId) {
        Ingredient ingredient = getById(ingredientId);
        double quantiteRecommandee = quantiteRecommandee(ingredient.getStockActuel(), ingredient.getSeuilAlerte());
        return ingredientDao.findBestPriceRecommendation(ingredientId, quantiteRecommandee)
                .orElseThrow(() -> new NotFoundException("Aucun fournisseur pour ingredient: " + ingredientId));
    }

    public AlerteStockDto toAlerteDto(Ingredient ingredient, Long commandeFournisseurId) {
        return new AlerteStockDto(
                ingredient,
                ingredient.getStockActuel(),
                ingredient.getSeuilAlerte(),
                quantiteRecommandee(ingredient.getStockActuel(), ingredient.getSeuilAlerte()),
                commandeFournisseurId
        );
    }

    private double quantiteRecommandee(double stockActuel, double seuilAlerte) {
        if (seuilAlerte > stockActuel) {
            return 2 * (seuilAlerte - stockActuel);
        }
        return seuilAlerte;
    }
}

package fr.epf.restaurant.controller;

import fr.epf.restaurant.controller.model.Ingredient;
import fr.epf.restaurant.dto.PrixIngredientDto;
import fr.epf.restaurant.dto.RecommandationDto;
import fr.epf.restaurant.service.IngredientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<Ingredient> lister() {
        return ingredientService.listerIngredients();
    }

    @GetMapping("/alertes")
    public List<Ingredient> alertes() {
        return ingredientService.getAlertes();
    }

    @GetMapping("/{id}/prix")
    public List<PrixIngredientDto> prix(@PathVariable long id) {
        return ingredientService.getPrix(id);
    }

    @GetMapping("/{id}/recommandation")
    public RecommandationDto recommandation(@PathVariable long id) {
        return ingredientService.getRecommandation(id);
    }
}

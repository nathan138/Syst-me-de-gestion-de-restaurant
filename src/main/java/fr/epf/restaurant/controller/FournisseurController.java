package fr.epf.restaurant.controller;

import fr.epf.restaurant.controller.model.Fournisseur;
import fr.epf.restaurant.dto.CataloguePrixDto;
import fr.epf.restaurant.service.FournisseurService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fournisseurs")
public class FournisseurController {
    private final FournisseurService fournisseurService;

    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @GetMapping
    public List<Fournisseur> lister() {
        return fournisseurService.listerFournisseurs();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fournisseur creer(@RequestBody Fournisseur fournisseur) {
        return fournisseurService.creer(fournisseur);
    }

    @GetMapping("/{id}/catalogue")
    public List<CataloguePrixDto> catalogue(@PathVariable long id) {
        return fournisseurService.getCatalogue(id);
    }
}

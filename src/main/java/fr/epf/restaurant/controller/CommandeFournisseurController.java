package fr.epf.restaurant.controller;

import fr.epf.restaurant.controller.model.CommandeFournisseur;
import fr.epf.restaurant.dto.CreerCommandeFournisseurRequest;
import fr.epf.restaurant.service.CommandeFournisseurService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/commandes/fournisseur")
public class CommandeFournisseurController {
    private final CommandeFournisseurService commandeFournisseurService;

    public CommandeFournisseurController(CommandeFournisseurService commandeFournisseurService) {
        this.commandeFournisseurService = commandeFournisseurService;
    }

    @GetMapping
    public List<CommandeFournisseur> lister(@RequestParam(required = false) String statut) {
        return commandeFournisseurService.lister(statut);
    }

    @GetMapping("/{id}")
    public CommandeFournisseur getById(@PathVariable long id) {
        return commandeFournisseurService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommandeFournisseur creer(@RequestBody CreerCommandeFournisseurRequest request) {
        return commandeFournisseurService.creer(request);
    }

    @PutMapping("/{id}/envoyer")
    public CommandeFournisseur envoyer(@PathVariable long id) {
        return commandeFournisseurService.envoyer(id);
    }

    @PutMapping("/{id}/recevoir")
    public CommandeFournisseur recevoir(@PathVariable long id) {
        return commandeFournisseurService.recevoir(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable long id) {
        commandeFournisseurService.supprimer(id);
    }
}

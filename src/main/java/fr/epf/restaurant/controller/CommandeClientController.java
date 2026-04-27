package fr.epf.restaurant.controller;

import fr.epf.restaurant.controller.model.CommandeClient;
import fr.epf.restaurant.dto.CreerCommandeClientRequest;
import fr.epf.restaurant.dto.PreparationResultDto;
import fr.epf.restaurant.service.CommandeClientService;
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
@RequestMapping("/api/commandes/client")
public class CommandeClientController {
    private final CommandeClientService commandeClientService;

    public CommandeClientController(CommandeClientService commandeClientService) {
        this.commandeClientService = commandeClientService;
    }

    @GetMapping
    public List<CommandeClient> lister(@RequestParam(required = false) String statut) {
        return commandeClientService.lister(statut);
    }

    @GetMapping("/{id}")
    public CommandeClient getById(@PathVariable long id) {
        return commandeClientService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommandeClient creer(@RequestBody CreerCommandeClientRequest request) {
        return commandeClientService.creer(request);
    }

    @PutMapping("/{id}/preparer")
    public PreparationResultDto preparer(@PathVariable long id) {
        return commandeClientService.preparer(id);
    }

    @PutMapping("/{id}/servir")
    public CommandeClient servir(@PathVariable long id) {
        return commandeClientService.servir(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable long id) {
        commandeClientService.supprimer(id);
    }
}

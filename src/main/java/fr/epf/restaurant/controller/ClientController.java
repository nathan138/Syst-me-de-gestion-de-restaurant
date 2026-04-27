package fr.epf.restaurant.controller;

import fr.epf.restaurant.controller.model.Client;
import fr.epf.restaurant.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> lister() {
        return clientService.listerClients();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client creer(@RequestBody Client client) {
        return clientService.creer(client);
    }
}

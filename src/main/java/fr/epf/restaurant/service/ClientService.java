package fr.epf.restaurant.service;

import fr.epf.restaurant.controller.model.Client;
import fr.epf.restaurant.dao.ClientDao;
import fr.epf.restaurant.exception.BadRequestException;
import fr.epf.restaurant.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {
    private final ClientDao clientDao;

    public ClientService(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public List<Client> listerClients() {
        return clientDao.findAll();
    }

    public Client getById(long id) {
        return clientDao.findById(id).orElseThrow(() -> new NotFoundException("Client introuvable: " + id));
    }

    @Transactional
    public Client creer(Client client) {
        if (client.getNom() == null || client.getNom().isBlank()) {
            throw new BadRequestException("Le nom est obligatoire");
        }
        if (client.getPrenom() == null || client.getPrenom().isBlank()) {
            throw new BadRequestException("Le prenom est obligatoire");
        }
        if (client.getEmail() == null || client.getEmail().isBlank()) {
            throw new BadRequestException("L'email est obligatoire");
        }
        return clientDao.insert(client);
    }
}

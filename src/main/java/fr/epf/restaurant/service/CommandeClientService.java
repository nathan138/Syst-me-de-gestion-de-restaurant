package fr.epf.restaurant.service;

import fr.epf.restaurant.controller.model.CommandeClient;
import fr.epf.restaurant.dao.ClientDao;
import fr.epf.restaurant.dao.CommandeClientDao;
import fr.epf.restaurant.dao.PlatDao;
import fr.epf.restaurant.dto.AlerteStockDto;
import fr.epf.restaurant.dto.CreerCommandeClientRequest;
import fr.epf.restaurant.dto.PreparationResultDto;
import fr.epf.restaurant.exception.BadRequestException;
import fr.epf.restaurant.exception.InvalidTransitionException;
import fr.epf.restaurant.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommandeClientService {
    private final CommandeClientDao commandeClientDao;
    private final ClientDao clientDao;
    private final PlatDao platDao;
    private final StockService stockService;
    private final IngredientService ingredientService;

    public CommandeClientService(
            CommandeClientDao commandeClientDao,
            ClientDao clientDao,
            PlatDao platDao,
            StockService stockService,
            IngredientService ingredientService
    ) {
        this.commandeClientDao = commandeClientDao;
        this.clientDao = clientDao;
        this.platDao = platDao;
        this.stockService = stockService;
        this.ingredientService = ingredientService;
    }

    public List<CommandeClient> lister(String statut) {
        List<CommandeClient> commandes = commandeClientDao.findAll(statut);
        commandes.forEach(this::enrich);
        return commandes;
    }

    public CommandeClient getById(long id) {
        CommandeClient commande = commandeClientDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Commande client introuvable: " + id));
        enrich(commande);
        return commande;
    }

    @Transactional
    public CommandeClient creer(CreerCommandeClientRequest request) {
        validateRequest(request);
        if (!clientDao.existsById(request.getClientId())) {
            throw new NotFoundException("Client introuvable: " + request.getClientId());
        }
        for (CreerCommandeClientRequest.LigneCommandeClientRequest ligne : request.getLignes()) {
            if (!platDao.existsById(ligne.getPlatId())) {
                throw new NotFoundException("Plat introuvable: " + ligne.getPlatId());
            }
        }

        long id = commandeClientDao.insert(request.getClientId(), LocalDateTime.now(), "EN_ATTENTE");
        for (CreerCommandeClientRequest.LigneCommandeClientRequest ligne : request.getLignes()) {
            commandeClientDao.insertLigne(id, ligne.getPlatId(), ligne.getQuantite());
        }
        return getById(id);
    }

    @Transactional
    public PreparationResultDto preparer(long id) {
        CommandeClient commande = getById(id);
        if (!"EN_ATTENTE".equals(commande.getStatut())) {
            throw new InvalidTransitionException("Seules les commandes EN_ATTENTE peuvent passer EN_PREPARATION");
        }

        Map<Long, Integer> platQuantites = new LinkedHashMap<>();
        commande.getLignes().forEach(l -> platQuantites.merge(l.getPlat().getId(), l.getQuantite(), Integer::sum));
        stockService.verifierEtConsommer(platQuantites);
        commandeClientDao.updateStatut(id, "EN_PREPARATION");
        CommandeClient updated = getById(id);

        List<AlerteStockDto> alertes = ingredientService.getAlertes()
                .stream()
                .map(ingredient -> ingredientService.toAlerteDto(ingredient, null))
                .toList();

        return new PreparationResultDto(updated, alertes);
    }

    @Transactional
    public CommandeClient servir(long id) {
        CommandeClient commande = getById(id);
        if (!"EN_PREPARATION".equals(commande.getStatut())) {
            throw new InvalidTransitionException("Seules les commandes EN_PREPARATION peuvent passer SERVIE");
        }
        commandeClientDao.updateStatut(id, "SERVIE");
        return getById(id);
    }

    @Transactional
    public void supprimer(long id) {
        getById(id);
        commandeClientDao.delete(id);
    }

    private void validateRequest(CreerCommandeClientRequest request) {
        if (request == null || request.getClientId() <= 0) {
            throw new BadRequestException("clientId invalide");
        }
        if (request.getLignes() == null || request.getLignes().isEmpty()) {
            throw new BadRequestException("La commande doit contenir au moins une ligne");
        }
        for (CreerCommandeClientRequest.LigneCommandeClientRequest ligne : request.getLignes()) {
            if (ligne.getPlatId() <= 0 || ligne.getQuantite() <= 0) {
                throw new BadRequestException("Lignes de commande invalides");
            }
        }
    }

    private void enrich(CommandeClient commande) {
        commande.setClient(clientDao.findById(commande.getClient().getId())
                .orElseThrow(() ->
                        new NotFoundException("Client introuvable: " + commande.getClient().getId())));
    }
}

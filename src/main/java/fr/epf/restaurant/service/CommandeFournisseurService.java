package fr.epf.restaurant.service;

import fr.epf.restaurant.controller.model.CommandeFournisseur;
import fr.epf.restaurant.dao.CommandeFournisseurDao;
import fr.epf.restaurant.dao.FournisseurDao;
import fr.epf.restaurant.dao.IngredientDao;
import fr.epf.restaurant.dto.CreerCommandeFournisseurRequest;
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
public class CommandeFournisseurService {
    private final CommandeFournisseurDao commandeFournisseurDao;
    private final FournisseurDao fournisseurDao;
    private final IngredientDao ingredientDao;
    private final StockService stockService;

    public CommandeFournisseurService(
            CommandeFournisseurDao commandeFournisseurDao,
            FournisseurDao fournisseurDao,
            IngredientDao ingredientDao,
            StockService stockService
    ) {
        this.commandeFournisseurDao = commandeFournisseurDao;
        this.fournisseurDao = fournisseurDao;
        this.ingredientDao = ingredientDao;
        this.stockService = stockService;
    }

    public List<CommandeFournisseur> lister(String statut) {
        List<CommandeFournisseur> commandes = commandeFournisseurDao.findAll(statut);
        commandes.forEach(this::enrich);
        return commandes;
    }

    public CommandeFournisseur getById(long id) {
        CommandeFournisseur commande = commandeFournisseurDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Commande fournisseur introuvable: " + id));
        enrich(commande);
        return commande;
    }

    @Transactional
    public CommandeFournisseur creer(CreerCommandeFournisseurRequest request) {
        validateRequest(request);
        if (!fournisseurDao.existsById(request.getFournisseurId())) {
            throw new NotFoundException("Fournisseur introuvable: " + request.getFournisseurId());
        }
        for (CreerCommandeFournisseurRequest.LigneCommandeFournisseurRequest ligne : request.getLignes()) {
            ingredientDao.findById(ligne.getIngredientId())
                    .orElseThrow(() -> new NotFoundException("Ingredient introuvable: " + ligne.getIngredientId()));
        }

        long id = commandeFournisseurDao.insert(request.getFournisseurId(), LocalDateTime.now(), "EN_ATTENTE");
        for (CreerCommandeFournisseurRequest.LigneCommandeFournisseurRequest ligne : request.getLignes()) {
            commandeFournisseurDao.insertLigne(
                    id,
                    ligne.getIngredientId(),
                    ligne.getQuantite(),
                    ligne.getPrixUnitaire()
            );
        }
        return getById(id);
    }

    @Transactional
    public CommandeFournisseur envoyer(long id) {
        CommandeFournisseur commande = getById(id);
        if (!"EN_ATTENTE".equals(commande.getStatut())) {
            throw new InvalidTransitionException("Seules les commandes EN_ATTENTE peuvent passer ENVOYEE");
        }
        commandeFournisseurDao.updateStatut(id, "ENVOYEE");
        return getById(id);
    }

    @Transactional
    public CommandeFournisseur recevoir(long id) {
        CommandeFournisseur commande = getById(id);
        if (!"ENVOYEE".equals(commande.getStatut())) {
            throw new InvalidTransitionException("Seules les commandes ENVOYEE peuvent passer RECUE");
        }

        Map<Long, Double> reappro = new LinkedHashMap<>();
        commande.getLignes().forEach(ligne ->
                reappro.merge(ligne.getIngredient().getId(), ligne.getQuantiteCommandee(), Double::sum)
        );
        stockService.reapprovisionner(reappro);
        commandeFournisseurDao.updateStatut(id, "RECUE");
        return getById(id);
    }

    @Transactional
    public void supprimer(long id) {
        getById(id);
        commandeFournisseurDao.delete(id);
    }

    private void validateRequest(CreerCommandeFournisseurRequest request) {
        if (request == null || request.getFournisseurId() <= 0) {
            throw new BadRequestException("fournisseurId invalide");
        }
        if (request.getLignes() == null || request.getLignes().isEmpty()) {
            throw new BadRequestException("La commande doit contenir au moins une ligne");
        }
        for (CreerCommandeFournisseurRequest.LigneCommandeFournisseurRequest ligne : request.getLignes()) {
            if (ligne.getIngredientId() <= 0 || ligne.getQuantite() <= 0 || ligne.getPrixUnitaire() == null) {
                throw new BadRequestException("Lignes de commande invalides");
            }
        }
    }

    private void enrich(CommandeFournisseur commande) {
        commande.setFournisseur(fournisseurDao.findById(commande.getFournisseur().getId())
                .orElseThrow(() ->
                        new NotFoundException("Fournisseur introuvable: " + commande.getFournisseur().getId())));
    }
}

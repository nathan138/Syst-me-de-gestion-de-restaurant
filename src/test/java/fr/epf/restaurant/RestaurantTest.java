package fr.epf.restaurant;

import fr.epf.restaurant.controller.model.Client;
import fr.epf.restaurant.controller.model.Fournisseur;
import fr.epf.restaurant.controller.model.Ingredient;
import fr.epf.restaurant.controller.model.Plat;
import fr.epf.restaurant.dao.IngredientDao;
import fr.epf.restaurant.dto.CreerCommandeClientRequest;
import fr.epf.restaurant.dto.CreerCommandeFournisseurRequest;
import fr.epf.restaurant.exception.InvalidTransitionException;
import fr.epf.restaurant.exception.NotFoundException;
import fr.epf.restaurant.exception.StockInsuffisantException;
import fr.epf.restaurant.service.ClientService;
import fr.epf.restaurant.service.CommandeClientService;
import fr.epf.restaurant.service.CommandeFournisseurService;
import fr.epf.restaurant.service.FournisseurService;
import fr.epf.restaurant.service.IngredientService;
import fr.epf.restaurant.service.PlatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
class RestaurantTest {

    @Autowired
    private ClientService clientService;
    @Autowired
    private PlatService platService;
    @Autowired
    private FournisseurService fournisseurService;
    @Autowired
    private CommandeClientService commandeClientService;
    @Autowired
    private CommandeFournisseurService commandeFournisseurService;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private IngredientDao ingredientDao;

    @Test
    void creerClient_retourneClientAvecId() {
        Client client = new Client("Nouveau", "Client", "nouveau@test.fr", "0600000001");
        Client created = clientService.creer(client);
        assertTrue(created.getId() > 0);
        assertEquals("Nouveau", created.getNom());
        assertEquals("nouveau@test.fr", created.getEmail());
    }

    @Test
    void creerPlat_retournePlatAvecId() {
        Plat plat = new Plat("Pizza Margherita", "Tomate et mozzarella", new BigDecimal("13.50"));
        Plat created = platService.creer(plat);
        assertTrue(created.getId() > 0);
        assertEquals("Pizza Margherita", created.getNom());
        assertEquals(0, new BigDecimal("13.50").compareTo(created.getPrix()));
    }

    @Test
    void creerFournisseur_retourneFournisseurAvecId() {
        Fournisseur f = new Fournisseur("NouveauFourn", "Contact", "fourn@test.fr");
        Fournisseur created = fournisseurService.creer(f);
        assertTrue(created.getId() > 0);
        assertEquals("NouveauFourn", created.getNom());
    }

    @Test
    void creerCommandeClient_clientInexistant_leveNotFoundException() {
        CreerCommandeClientRequest request = new CreerCommandeClientRequest(
                9999L,
                List.of(new CreerCommandeClientRequest.LigneCommandeClientRequest(1L, 1))
        );
        assertThrows(NotFoundException.class, () -> commandeClientService.creer(request));
    }

    @Test
    void creerCommandeClient_platInexistant_leveNotFoundException() {
        CreerCommandeClientRequest request = new CreerCommandeClientRequest(
                1L,
                List.of(new CreerCommandeClientRequest.LigneCommandeClientRequest(9999L, 1))
        );
        assertThrows(NotFoundException.class, () -> commandeClientService.creer(request));
    }

    @Test
    void preparerCommande_stockInsuffisant_leveStockInsuffisantException() {
        // Quiche Lorraine (id=1) requiert 200g pate brisee par portion ; stock = 500g
        // 10 quiches = 2000g > 500g stock disponible
        CreerCommandeClientRequest request = new CreerCommandeClientRequest(
                1L,
                List.of(new CreerCommandeClientRequest.LigneCommandeClientRequest(1L, 10))
        );
        long commandeId = commandeClientService.creer(request).getId();
        assertThrows(StockInsuffisantException.class, () -> commandeClientService.preparer(commandeId));
    }

    @Test
    void preparerCommande_dejaEnPreparation_leveInvalidTransitionException() {
        // 1 quiche = 200g pate brisee (stock 500g), OK
        CreerCommandeClientRequest request = new CreerCommandeClientRequest(
                1L,
                List.of(new CreerCommandeClientRequest.LigneCommandeClientRequest(1L, 1))
        );
        long commandeId = commandeClientService.creer(request).getId();
        commandeClientService.preparer(commandeId);
        assertThrows(InvalidTransitionException.class, () -> commandeClientService.preparer(commandeId));
    }

    @Test
    void servirCommande_enAttente_leveInvalidTransitionException() {
        CreerCommandeClientRequest request = new CreerCommandeClientRequest(
                1L,
                List.of(new CreerCommandeClientRequest.LigneCommandeClientRequest(1L, 1))
        );
        long commandeId = commandeClientService.creer(request).getId();
        assertThrows(InvalidTransitionException.class, () -> commandeClientService.servir(commandeId));
    }

    @Test
    void alertesStock_ingredientSousSeuil_retourneAlerte() {
        // Pommes de terre (id=7) : seuil=500, on baisse le stock à 100 (< seuil)
        ingredientDao.updateStock(7L, 100.0);
        List<Ingredient> alertes = ingredientService.getAlertes();
        assertTrue(alertes.stream().anyMatch(i -> i.getId() == 7L));
    }

    @Test
    void recevoirCommandeFournisseur_metsAJourLeStock() {
        double stockInitial = ingredientDao.findById(1L).orElseThrow().getStockActuel();

        CreerCommandeFournisseurRequest request = new CreerCommandeFournisseurRequest(
                1L,
                List.of(new CreerCommandeFournisseurRequest.LigneCommandeFournisseurRequest(
                        1L, 200.0, new BigDecimal("0.06")
                ))
        );
        long commandeId = commandeFournisseurService.creer(request).getId();
        commandeFournisseurService.envoyer(commandeId);
        commandeFournisseurService.recevoir(commandeId);

        double stockFinal = ingredientDao.findById(1L).orElseThrow().getStockActuel();
        assertEquals(stockInitial + 200.0, stockFinal, 0.001);
    }
}

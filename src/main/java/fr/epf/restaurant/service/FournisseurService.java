package fr.epf.restaurant.service;

import fr.epf.restaurant.controller.model.Fournisseur;
import fr.epf.restaurant.dao.FournisseurDao;
import fr.epf.restaurant.dto.CataloguePrixDto;
import fr.epf.restaurant.exception.BadRequestException;
import fr.epf.restaurant.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FournisseurService {
    private final FournisseurDao fournisseurDao;

    public FournisseurService(FournisseurDao fournisseurDao) {
        this.fournisseurDao = fournisseurDao;
    }

    public List<Fournisseur> listerFournisseurs() {
        return fournisseurDao.findAll();
    }

    public Fournisseur getById(long id) {
        return fournisseurDao.findById(id).orElseThrow(() -> new NotFoundException("Fournisseur introuvable: " + id));
    }

    public List<CataloguePrixDto> getCatalogue(long fournisseurId) {
        if (!fournisseurDao.existsById(fournisseurId)) {
            throw new NotFoundException("Fournisseur introuvable: " + fournisseurId);
        }
        return fournisseurDao.findCatalogueByFournisseur(fournisseurId);
    }

    @Transactional
    public Fournisseur creer(Fournisseur fournisseur) {
        if (fournisseur.getNom() == null || fournisseur.getNom().isBlank()) {
            throw new BadRequestException("Le nom est obligatoire");
        }
        if (fournisseur.getEmail() == null || fournisseur.getEmail().isBlank()) {
            throw new BadRequestException("L'email est obligatoire");
        }
        return fournisseurDao.insert(fournisseur);
    }
}

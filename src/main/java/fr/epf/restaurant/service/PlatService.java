package fr.epf.restaurant.service;

import fr.epf.restaurant.controller.model.Plat;
import fr.epf.restaurant.dao.PlatDao;
import fr.epf.restaurant.exception.BadRequestException;
import fr.epf.restaurant.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PlatService {
    private final PlatDao platDao;

    public PlatService(PlatDao platDao) {
        this.platDao = platDao;
    }

    public List<Plat> listerPlats() {
        return platDao.findAll();
    }

    public Plat getPlatDetail(long id) {
        Plat plat = platDao.findById(id).orElseThrow(() -> new NotFoundException("Plat introuvable: " + id));
        plat.setIngredients(platDao.findIngredientsForPlat(id));
        return plat;
    }

    @Transactional
    public Plat creer(Plat plat) {
        if (plat.getNom() == null || plat.getNom().isBlank()) {
            throw new BadRequestException("Le nom est obligatoire");
        }
        if (plat.getPrix() == null || plat.getPrix().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Le prix doit etre superieur a 0");
        }
        return platDao.insert(plat);
    }
}

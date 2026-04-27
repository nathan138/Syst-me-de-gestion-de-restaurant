package fr.epf.restaurant.controller;

import fr.epf.restaurant.controller.model.Plat;
import fr.epf.restaurant.service.PlatService;
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
@RequestMapping("/api/plats")
public class PlatController {
    private final PlatService platService;

    public PlatController(PlatService platService) {
        this.platService = platService;
    }

    @GetMapping
    public List<Plat> lister() {
        return platService.listerPlats();
    }

    @GetMapping("/{id}")
    public Plat detail(@PathVariable long id) {
        return platService.getPlatDetail(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Plat creer(@RequestBody Plat plat) {
        return platService.creer(plat);
    }
}

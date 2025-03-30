package es.upm.miw.companyds.tfm_spring.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseSeederService {

    private final EntitySeederService entitySeederService;

    @Autowired
    public DatabaseSeederService(
            EntitySeederService entitySeederService
    ) {
        this.entitySeederService = entitySeederService;
        this.seedDatabase();
    }

    public void seedDatabase() {
        this.entitySeederService.seedDatabase();
    }

    public void deleteAll() {
        this.entitySeederService.deleteAll();
        this.seedDatabase();
    }

    public void reSeedDatabase() {
        this.deleteAll();
        this.seedDatabase();
    }
}

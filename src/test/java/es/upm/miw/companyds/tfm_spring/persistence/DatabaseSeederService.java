package es.upm.miw.companyds.tfm_spring.persistence;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseSeederService {

    private final UserSeederService userSeederService;

    @Autowired
    public DatabaseSeederService(
            UserSeederService userSeederService
    ) {
        this.userSeederService = userSeederService;
        this.seedDatabase();
    }

    public void seedDatabase() {
        this.userSeederService.seedDatabase();
    }

    public void deleteAll() {
        this.userSeederService.deleteAll();
        this.seedDatabase();
    }

    public void reSeedDatabase() {
        this.deleteAll();
        this.seedDatabase();
    }
}

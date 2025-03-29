package es.upm.miw.companyds.tfm_spring.persistence.repository;

import es.upm.miw.companyds.tfm_spring.TestConfig;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

}

package es.upm.miw.companyds.tfm_spring.persistence.repository;

import es.upm.miw.companyds.tfm_spring.TestConfig;
import es.upm.miw.companyds.tfm_spring.persistence.model.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestConfig
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void findAllByUserPhone() {
        List<Address> list = this.addressRepository.findAllByUserPhone("677222333");
        assertNotNull(list);
        assertEquals(list.size(), 1);
    }
}

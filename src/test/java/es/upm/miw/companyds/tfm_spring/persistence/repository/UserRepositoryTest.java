package es.upm.miw.companyds.tfm_spring.persistence.repository;

import es.upm.miw.companyds.tfm_spring.TestConfig;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testReadByPhone() {
        assertTrue(this.userRepository.findByPhone("677222333").isPresent());
        User user = this.userRepository.findByPhone("677222333").get();
        assertEquals("Maria", user.getFirstName());
        assertEquals("87654321B", user.getDni());
    }

}

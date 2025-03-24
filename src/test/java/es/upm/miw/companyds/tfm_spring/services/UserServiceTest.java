package es.upm.miw.companyds.tfm_spring.services;

import es.upm.miw.companyds.tfm_spring.TestConfig;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import es.upm.miw.companyds.tfm_spring.persistence.repository.UserRepository;
import es.upm.miw.companyds.tfm_spring.services.impl.ConflictException;
import es.upm.miw.companyds.tfm_spring.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void testRegisterUser() {
        UserDto userDto = UserDto.builder()
                .phone("666000666")
                .firstName("Juan")
                .familyName("Pérez")
                .dni("12121212K")
                .email("juan@example.com")
                .password("password123")
                .build();

        userService.registerUser(userDto);

        Optional<User> savedUserOpt = userRepository.findByPhone("666000666");
        assertTrue(savedUserOpt.isPresent());
        User savedUser = savedUserOpt.get();
        assertEquals("666000666", savedUser.getPhone());
        assertEquals(Role.CUSTOMER, savedUser.getRole());
    }
}

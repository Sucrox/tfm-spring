package es.upm.miw.companyds.tfm_spring.persistence;


import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import es.upm.miw.companyds.tfm_spring.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserSeederService {

    @Autowired
    private UserRepository userRepository;

    public void seedDatabase() {
        LogManager.getLogger(this.getClass()).warn("------- User Initial Load -----------");
        UserDto[] userDtos = {
                UserDto.builder()
                        .phone("666111222")
                        .firstName("Juan")
                        .familyName("Perez")
                        .email("juan.perez@example.com")
                        .dni("12345678A")
                        .password("password123")
                        .role(Role.ADMIN)
                        .build(),
                UserDto.builder()
                        .phone("677222333")
                        .firstName("Maria")
                        .familyName("Gomez")
                        .email("maria.gomez@example.com")
                        .dni("87654321B")
                        .password("password456")
                        .build()
        };
        List<User> users = Arrays.stream(userDtos)
                .map(UserDto::toUser)
                .toList();
        this.userRepository.saveAll(users);
    }

    public void deleteAll() {
        this.userRepository.deleteAll();

    }

}

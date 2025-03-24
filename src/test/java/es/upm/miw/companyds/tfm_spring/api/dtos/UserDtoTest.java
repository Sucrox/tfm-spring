package es.upm.miw.companyds.tfm_spring.api.dtos;

import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDtoTest {

    @Test
    void testUserDtoBuilder() {
        UserDto userDto = UserDto.builder()
                .phone("666666666")
                .firstName("daemon")
                .familyName("example")
                .email("daemon@example.com")
                .dni("12345678A")
                .role(Role.ADMIN)
                .password("defaultPassword")
                .build();
        assertEquals("666666666", userDto.getPhone());
        assertEquals("daemon@example.com", userDto.getEmail());
        assertEquals(Role.ADMIN, userDto.getRole());
    }

    @Test
    void testToUser() {
        UserDto userDto = UserDto.builder()
                .phone("666666666")
                .firstName("daemon")
                .familyName("example")
                .email("daemon@example.com")
                .dni("12345678A")
                .password("defaultPassword")
                .build();
        User user = userDto.toUser();
        assertEquals(Role.CUSTOMER, user.getRole());
    }
}

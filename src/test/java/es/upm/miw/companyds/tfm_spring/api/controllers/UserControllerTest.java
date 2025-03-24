package es.upm.miw.companyds.tfm_spring.api.controllers;

import es.upm.miw.companyds.tfm_spring.api.dto.LoginDto;
import es.upm.miw.companyds.tfm_spring.api.dto.TokenDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ApiTestConfig
public class UserControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testRegisterUser() {
        UserDto userDto = UserDto.builder()
                .phone("699888521")
                .firstName("Lucas")
                .familyName("Fernandez")
                .email("lucas.fernandez@example.com")
                .dni("12345671Z")
                .password("pass123")
                .build();

        ResponseEntity<Void> response = testRestTemplate.postForEntity("/users/register", userDto, Void.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testLoginUser() {
        LoginDto loginDto = LoginDto.builder()
                .email("juan.perez@example.com")
                .password("password123")
                .build();

        ResponseEntity<TokenDto> response =testRestTemplate.postForEntity("/users/login", loginDto, TokenDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
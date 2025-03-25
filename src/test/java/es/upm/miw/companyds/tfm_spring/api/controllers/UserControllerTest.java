package es.upm.miw.companyds.tfm_spring.api.controllers;

import es.upm.miw.companyds.tfm_spring.api.dto.LoginDto;
import es.upm.miw.companyds.tfm_spring.api.dto.TokenDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import es.upm.miw.companyds.tfm_spring.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
public class UserControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

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

    @Test
    void testGetAllUsers() {
        HttpHeaders headers = authenticateUser();

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<UserDto[]> response = testRestTemplate.exchange("/users", HttpMethod.GET, request, UserDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 1);
    }

    @Test
    void testGetUserById() {
        HttpHeaders headers = authenticateUser();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        assertTrue(this.userRepository.findByPhone("666111222").isPresent());
        User user = this.userRepository.findByPhone("666111222").get();

        ResponseEntity<UserDto> response = testRestTemplate.exchange("/users/" + user.getId(), HttpMethod.GET, request, UserDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    private HttpHeaders authenticateUser() {
        LoginDto loginDto = LoginDto.builder().email("juan.perez@example.com").password("password123").build();
        ResponseEntity<TokenDto> response = testRestTemplate.postForEntity("/users/login", loginDto, TokenDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + response.getBody().getToken());
        return headers;
    }
}
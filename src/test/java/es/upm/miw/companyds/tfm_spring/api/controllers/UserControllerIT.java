package es.upm.miw.companyds.tfm_spring.api.controllers;

import es.upm.miw.companyds.tfm_spring.api.dto.LoginDto;
import es.upm.miw.companyds.tfm_spring.api.dto.TokenDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateUserDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import es.upm.miw.companyds.tfm_spring.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
public class UserControllerIT {

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

    @Test
    void testCreateUser() {
        HttpHeaders headers = authenticateUser();
        UserDto userDto= UserDto.builder()
                .phone("616333321")
                .firstName("Controller")
                .familyName("Controller")
                .email("controller@example.com")
                .dni("87123333B")
                .password("456")
                .build();

        HttpEntity<UserDto> request = new HttpEntity<>(userDto, headers);

        ResponseEntity<UserDto> response = testRestTemplate.exchange("/users", HttpMethod.POST, request, UserDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateUser() {
        HttpHeaders headers = authenticateUser();
        UpdateUserDto updateUserDto= UpdateUserDto.builder()
                .familyName("Update")
                .build();

        assertTrue(this.userRepository.findByPhone("616333625").isPresent());
        User user = this.userRepository.findByPhone("616333625").get();

        HttpEntity<UpdateUserDto> request = new HttpEntity<>(updateUserDto, headers);

        ResponseEntity<UserDto> response = testRestTemplate.exchange("/users/" + user.getId(), HttpMethod.PATCH, request, UserDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDeleteUSer() {
        HttpHeaders headers = authenticateUser();

        HttpEntity<UpdateUserDto> request = new HttpEntity<>( headers);

        assertTrue(this.userRepository.findByPhone("616312333").isPresent());
        User user = this.userRepository.findByPhone("616312333").get();

        ResponseEntity<Void> response = testRestTemplate.exchange("/users/" + user.getId(), HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
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
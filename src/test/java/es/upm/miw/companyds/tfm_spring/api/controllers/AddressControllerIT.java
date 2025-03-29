package es.upm.miw.companyds.tfm_spring.api.controllers;

import es.upm.miw.companyds.tfm_spring.api.dto.*;
import es.upm.miw.companyds.tfm_spring.persistence.model.Address;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import es.upm.miw.companyds.tfm_spring.persistence.repository.AddressRepository;
import es.upm.miw.companyds.tfm_spring.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
public class AddressControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testGetAddressesById() {
        HttpHeaders headers = authenticateUser();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        assertTrue(this.userRepository.findByPhone("677222333").isPresent());
        User user = this.userRepository.findByPhone("677222333").get();

        ResponseEntity<AddressDto[]> response = testRestTemplate.exchange("/address/user/" + user.getId(), HttpMethod.GET, request, AddressDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    void testUpdateAddress() {
        HttpHeaders headers = authenticateUser();
        UpdateAddressDto updateAddressDto= UpdateAddressDto.builder()
                .floor("fllor5")
                .build();

        Address address = this.addressRepository.findAll().get(1);

        HttpEntity<UpdateAddressDto> request = new HttpEntity<>(updateAddressDto, headers);

        ResponseEntity<AddressDto> response = testRestTemplate.exchange("/address/" + address.getId(), HttpMethod.PATCH, request, AddressDto.class);

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

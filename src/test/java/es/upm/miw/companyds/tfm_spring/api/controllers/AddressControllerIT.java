package es.upm.miw.companyds.tfm_spring.api.controllers;

import es.upm.miw.companyds.tfm_spring.api.dto.*;
import es.upm.miw.companyds.tfm_spring.persistence.model.Address;
import es.upm.miw.companyds.tfm_spring.persistence.repository.AddressRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
class AddressControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AddressRepository addressRepository;


    @Test
    void testGetAddressesByUserPhone() {
        HttpHeaders headers = authenticateUser();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<AddressDto[]> response = testRestTemplate.exchange("/address/user/" + "677222333", HttpMethod.GET, request, AddressDto[].class);

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

package es.upm.miw.companyds.tfm_spring.api.controllers;

import es.upm.miw.companyds.tfm_spring.api.dto.*;
import es.upm.miw.companyds.tfm_spring.persistence.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
class ProductControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testGetAllProducts() {
        HttpHeaders headers = authenticateUser();

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<PagedResponse<ProductDto>> response = testRestTemplate.exchange(
                "/products",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getList());
        assertTrue(response.getBody().getList().size() > 1);
    }

    @Test
    void testGetProductByBarcode() {
        HttpHeaders headers = authenticateUser();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<ProductDto> response = testRestTemplate.exchange("/products/" + "5556667778889", HttpMethod.GET, request, ProductDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreateProduct() {
        HttpHeaders headers = authenticateUser();
        ProductDto productDto = ProductDto.builder()
                .name("Green Tea")
                .barcode("1122334455667")
                .price(BigDecimal.valueOf(2.99))
                .quantity(120)
                .category(Category.BEVERAGES)
                .description("Refreshing green tea with antioxidants and a smooth taste")
                .build();

        HttpEntity<ProductDto> request = new HttpEntity<>(productDto, headers);

        ResponseEntity<ProductDto> response = testRestTemplate.exchange("/products", HttpMethod.POST, request, ProductDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateProduct() {
        HttpHeaders headers = authenticateUser();
        UpdateProductDto updateProductDto= UpdateProductDto.builder()
                .quantity(306)
                .build();

        HttpEntity<UpdateProductDto> request = new HttpEntity<>(updateProductDto, headers);

        ResponseEntity<ProductDto> response = testRestTemplate.exchange("/products/" + "1112223334445", HttpMethod.PATCH, request, ProductDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDeleteProduct() {
        HttpHeaders headers = authenticateUser();

        HttpEntity<UpdateUserDto> request = new HttpEntity<>( headers);

        ResponseEntity<Void> response = testRestTemplate.exchange("/products/" + "7778889990000", HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }


    private HttpHeaders authenticateUser() {
        LoginDto loginDto = LoginDto.builder().email("juan.perez@example.com").password("password123").build();
        ResponseEntity<LoginResponseDto> response = testRestTemplate.postForEntity("/users/login", loginDto, LoginResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + response.getBody().getToken());
        return headers;
    }
}

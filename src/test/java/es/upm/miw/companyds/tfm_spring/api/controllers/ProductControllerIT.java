package es.upm.miw.companyds.tfm_spring.api.controllers;

import es.upm.miw.companyds.tfm_spring.api.dto.*;
import es.upm.miw.companyds.tfm_spring.persistence.model.Category;
import es.upm.miw.companyds.tfm_spring.persistence.model.Product;
import es.upm.miw.companyds.tfm_spring.persistence.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ApiTestConfig
public class ProductControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testGetAllProducts() {
        HttpHeaders headers = authenticateUser();

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ProductDto[]> response = testRestTemplate.exchange("/products", HttpMethod.GET, request, ProductDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 1);
    }

    @Test
    void testGetProductByBarcode() {
        HttpHeaders headers = authenticateUser();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        assertTrue(this.productRepository.findByBarcode("5556667778889").isPresent());
        Product product = this.productRepository.findByBarcode("5556667778889").get();

        ResponseEntity<ProductDto> response = testRestTemplate.exchange("/products/" + product.getBarcode(), HttpMethod.GET, request, ProductDto.class);

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

        assertTrue(this.productRepository.findByBarcode("7778889990001").isPresent());
        Product product = this.productRepository.findByBarcode("7778889990001").get();

        HttpEntity<UpdateProductDto> request = new HttpEntity<>(updateProductDto, headers);

        ResponseEntity<ProductDto> response = testRestTemplate.exchange("/products/" + product.getBarcode(), HttpMethod.PATCH, request, ProductDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDeleteProduct() {
        HttpHeaders headers = authenticateUser();

        HttpEntity<UpdateUserDto> request = new HttpEntity<>( headers);

        assertTrue(this.productRepository.findByBarcode("7778889990000").isPresent());
        Product product = this.productRepository.findByBarcode("7778889990000").get();

        ResponseEntity<Void> response = testRestTemplate.exchange("/products/" + product.getBarcode(), HttpMethod.DELETE, request, Void.class);

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

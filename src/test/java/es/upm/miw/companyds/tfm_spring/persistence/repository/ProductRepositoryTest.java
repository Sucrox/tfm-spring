package es.upm.miw.companyds.tfm_spring.persistence.repository;

import es.upm.miw.companyds.tfm_spring.TestConfig;
import es.upm.miw.companyds.tfm_spring.persistence.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testRadByBarcode() {
        assertTrue(this.productRepository.findByBarcode("1112223334445").isPresent());
        Product product = this.productRepository.findByBarcode("1112223334445").get();
        assertEquals("Golden Apple", product.getName());
        assertEquals(BigDecimal.valueOf(2.99), product.getPrice());
    }
}

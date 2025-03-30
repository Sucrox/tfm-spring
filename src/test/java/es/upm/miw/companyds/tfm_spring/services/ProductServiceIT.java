package es.upm.miw.companyds.tfm_spring.services;

import es.upm.miw.companyds.tfm_spring.TestConfig;
import es.upm.miw.companyds.tfm_spring.api.dto.ProductDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateProductDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Category;
import es.upm.miw.companyds.tfm_spring.persistence.model.Product;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.repository.ProductRepository;
import es.upm.miw.companyds.tfm_spring.services.exceptions.ConflictException;
import es.upm.miw.companyds.tfm_spring.services.exceptions.ForbiddenException;
import es.upm.miw.companyds.tfm_spring.services.exceptions.NotFoundException;
import es.upm.miw.companyds.tfm_spring.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class ProductServiceIT {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceImpl productService;

    @Test
    void testGetProductCorrectBarcode() {
        Product product = productRepository.findByBarcode("5556667778889")
                .orElseThrow(() -> new RuntimeException("Test product not found"));
        ProductDto productDto = productService.getProductByBarcode(product.getBarcode(), Role.ADMIN);

        assertNotNull(productDto);
        assertEquals("Cheddar Cheese", productDto.getName());
    }

    @Test
    void testGetProductWrongBarcode() {
        assertThrows(NotFoundException.class, () -> productService.getProductByBarcode("-1", Role.ADMIN));
    }

    @Test
    void testGetProducts() {
        Stream<ProductDto> productDtoStream = productService.getAllProducts();
        List<ProductDto> productDtoList = productDtoStream.toList();
        assertTrue(productDtoList.size() > 1);
    }

    @Test
    void testCreateProduct() {
        ProductDto productDto = ProductDto.builder()
                .name("Carrot")
                .barcode("3334445556667")
                .price(BigDecimal.valueOf(1.99))
                .quantity(80)
                .category(Category.VEGETABLES)
                .description("Fresh organic carrots, rich in beta-carotene and vitamins")
                .build();
        assertEquals(productDto.getName(), this.productService.createProduct(productDto,Role.ADMIN).getName());
    }

    @Test
    void testCreateProductExceptions() {
        ProductDto productDto =             ProductDto.builder()
                .name("Cheddar Cheese")
                .barcode("5556667778889")
                .price(BigDecimal.valueOf(5.49))
                .quantity(30)
                .category(Category.DAIRY)
                .description("Aged cheddar cheese with rich and creamy flavor")
                .build();
        assertThrows(ForbiddenException.class, () -> productService.createProduct(productDto, Role.CUSTOMER));
        assertThrows(ConflictException.class, () -> productService.createProduct(productDto, Role.ADMIN));
    }

    @Test
    void testUpdateProduct() {
        assertTrue(this.productRepository.findByBarcode("5556667778889").isPresent());
        Product product = this.productRepository.findByBarcode("5556667778889").get();
        UpdateProductDto updateProductDto = UpdateProductDto.builder()
                .quantity(96)
                .build();
        assertEquals(updateProductDto.getQuantity(), productService.updateProduct(product.getBarcode(),updateProductDto, Role.ADMIN).getQuantity());
    }

    @Test
    void testUpdateProductExceptions() {
        assertTrue(this.productRepository.findByBarcode("5556667778889").isPresent());
        Product product = this.productRepository.findByBarcode("5556667778889").get();
        UpdateProductDto updateProductDto = UpdateProductDto.builder()
                .quantity(96)
                .build();
        assertThrows(NotFoundException.class, () -> productService.updateProduct("-1",updateProductDto, Role.ADMIN));
        assertThrows(ForbiddenException.class, () -> productService.updateProduct(product.getBarcode(), updateProductDto, Role.CUSTOMER));
    }


    @Test
    void testDeleteProduct() {
        assertTrue(this.productRepository.findByBarcode("7778889990001").isPresent());
        Product product = this.productRepository.findByBarcode("7778889990001").get();
        productService.deleteProduct(product.getBarcode(), Role.ADMIN);
        assertFalse(this.productRepository.findByBarcode("7778889990001").isPresent());
    }

    @Test
    void testDeleteProductExceptions() {
        assertThrows(NotFoundException.class, () -> productService.deleteProduct("-1", Role.ADMIN));
        assertThrows(ForbiddenException.class, () -> productService.deleteProduct("-1", Role.CUSTOMER));
    }

}

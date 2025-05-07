package es.upm.miw.companyds.tfm_spring.api.controller;

import es.upm.miw.companyds.tfm_spring.api.dto.ProductDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateProductDto;
import es.upm.miw.companyds.tfm_spring.services.impl.AuthorizationService;
import es.upm.miw.companyds.tfm_spring.services.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping(ProductController.PRODUCTS)
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("authenticated")
public class ProductController {

    public static final String PRODUCTS = "/products";
    public static final String PRODUCT_BARCODE= "/{barcode}";

    private final ProductServiceImpl productService;
    private final AuthorizationService authorizationService;

    @Autowired
    public ProductController(ProductServiceImpl productService, AuthorizationService authorizationService) {
        this.productService = productService;
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public ResponseEntity<Stream<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(this.productService.getAllProducts());
    }

    @GetMapping(PRODUCT_BARCODE)
    public ResponseEntity<ProductDto> getProduct(@PathVariable String barcode) {
        return ResponseEntity.ok(this.productService.getProductByBarcode(barcode));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto response = this.productService.createProduct(productDto, this.authorizationService.extractRoleClaims());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping(PRODUCT_BARCODE)
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String barcode, @RequestBody UpdateProductDto updateProductDto) {
        return ResponseEntity.ok(this.productService.updateProduct(barcode, updateProductDto, this.authorizationService.extractRoleClaims()));
    }

    @DeleteMapping(PRODUCT_BARCODE)
    public  ResponseEntity<Void> deleteProduct(@PathVariable String  barcode) {
        this.productService.deleteProduct(barcode, this.authorizationService.extractRoleClaims());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

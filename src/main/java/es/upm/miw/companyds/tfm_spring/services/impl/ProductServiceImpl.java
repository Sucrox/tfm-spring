package es.upm.miw.companyds.tfm_spring.services.impl;

import es.upm.miw.companyds.tfm_spring.api.dto.ProductDto;

import es.upm.miw.companyds.tfm_spring.api.dto.UpdateProductDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Product;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.repository.ProductRepository;
import es.upm.miw.companyds.tfm_spring.services.ProductService;
import es.upm.miw.companyds.tfm_spring.services.exceptions.ConflictException;
import es.upm.miw.companyds.tfm_spring.services.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final AuthorizationService authorizationService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, AuthorizationService authorizationService) {
        this.productRepository = productRepository;
        this.authorizationService = authorizationService;
    }

    @Override
    public Stream<ProductDto> getAllProducts(Role role) {
        return productRepository.findAll().stream().map(ProductDto::ofProduct);
    }

    @Override
    public ProductDto getProductByBarcode(String barcode, Role role) {
        return productRepository.findByBarcode(barcode).map(ProductDto::ofProduct)
                .orElseThrow(() -> new NotFoundException("There's no product with barcode:" + barcode));
    }

    @Transactional
    @Override
    public ProductDto createProduct(ProductDto productDto, Role role) {
        this.authorizationService.checkIfAuthorized(role);
        if (this.productRepository.findByBarcode(productDto.getBarcode()).isPresent()) {
            throw new ConflictException("Product already exists");
        }
        return ProductDto.ofProduct(this.productRepository.save(productDto.toProduct()));
    }

    @Transactional
    @Override
    public ProductDto updateProduct(String barcode, UpdateProductDto updateProductDto, Role role) {
        this.authorizationService.checkIfAuthorized(role);

        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new NotFoundException("Product doesn't exist"));

        Optional.ofNullable(updateProductDto.getName()).filter(name -> !name.isEmpty()).ifPresent(product::setName);
        Optional.ofNullable(updateProductDto.getDescription()).filter(description -> !description.isEmpty()).ifPresent(product::setDescription);
        Optional.ofNullable(updateProductDto.getPrice()).ifPresent(product::setPrice);
        Optional.of(updateProductDto.getQuantity()).ifPresent(product::setQuantity);
        Optional.ofNullable(updateProductDto.getExpirationDate()).ifPresent(product::setExpirationDate);

        return ProductDto.ofProduct(this.productRepository.save(product));
    }

    @Transactional
    @Override
    public void deleteProduct(String barcode, Role role) {
        this.authorizationService.checkIfAuthorized(role);
        productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new NotFoundException("Product does not exist"));
        productRepository.deleteByBarcode(barcode);
    }

}

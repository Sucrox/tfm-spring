package es.upm.miw.companyds.tfm_spring.services;

import es.upm.miw.companyds.tfm_spring.api.dto.ProductDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateProductDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {

    Page<ProductDto> getAllProducts(Pageable pageable);
    ProductDto getProductByBarcode(String barcode);
    ProductDto createProduct(ProductDto productDto, Role role);
    ProductDto updateProduct(String barcode, UpdateProductDto updateProductDto, Role role);
    void deleteProduct(String barcode, Role role);

}

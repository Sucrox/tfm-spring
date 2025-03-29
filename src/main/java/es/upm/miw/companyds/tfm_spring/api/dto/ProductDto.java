package es.upm.miw.companyds.tfm_spring.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.companyds.tfm_spring.persistence.model.Category;
import es.upm.miw.companyds.tfm_spring.persistence.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private String name;
    private BigDecimal price;
    private int quantity;
    private String description;
    private String barcode;
    private LocalDate expirationDate;
    private Category category;

    public static ProductDto ofProduct(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .barcode(product.getBarcode())
                .expirationDate(product.getExpirationDate())
                .category(product.getCategory())
                .quantity(product.getQuantity())
                .build();
    }

    public Product toProduct() {
        Product product = new Product();
        BeanUtils.copyProperties(this, product);
        return product;
    }
}

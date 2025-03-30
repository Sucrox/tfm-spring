package es.upm.miw.companyds.tfm_spring.persistence;

import es.upm.miw.companyds.tfm_spring.api.dto.AddressDto;
import es.upm.miw.companyds.tfm_spring.api.dto.ProductDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.*;
import es.upm.miw.companyds.tfm_spring.persistence.repository.AddressRepository;
import es.upm.miw.companyds.tfm_spring.persistence.repository.ProductRepository;
import es.upm.miw.companyds.tfm_spring.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class EntitySeederService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ProductRepository productRepository;


    public void seedDatabase() {
        if (userRepository.count() == 0) {
            LogManager.getLogger(this.getClass()).warn("------- User Initial Load -----------");
            UserDto[] userDtos = {
                    UserDto.builder()
                            .phone("666111222")
                            .firstName("Juan")
                            .familyName("Perez")
                            .email("juan.perez@example.com")
                            .dni("12345678A")
                            .password("password123")
                            .role(Role.ADMIN)
                            .build(),
                    UserDto.builder()
                            .phone("677222333")
                            .firstName("Maria")
                            .familyName("Gomez")
                            .email("maria.gomez@example.com")
                            .dni("87654321B")
                            .password("password456")
                            .build(),
                    UserDto.builder()
                            .phone("616333625")
                            .firstName("Update")
                            .familyName("Update")
                            .email("update@example.com")
                            .dni("87964333B")
                            .password("456")
                            .build(),
                    UserDto.builder()
                            .phone("616333999")
                            .firstName("Delete")
                            .familyName("Delete")
                            .email("delete@example.com")
                            .dni("87654333B")
                            .password("456")
                            .build(),
                    UserDto.builder()
                            .phone("616312333")
                            .firstName("Delete")
                            .familyName("Delete")
                            .email("delete2@example.com")
                            .dni("87654333Ñ")
                            .password("456")
                            .build()
            };
            List<User> users = Arrays.stream(userDtos)
                    .map(UserDto::toUser)
                    .toList();
            this.userRepository.saveAll(users);

            LogManager.getLogger(this.getClass()).warn("------- Address Initial Load -----------");

            AddressDto[] addressesDtos = {
                    AddressDto.builder()
                            .street("Main St")
                            .number("123")
                            .floor("2")
                            .door("A")
                            .postalCode("28001")
                            .city("Madrid")
                            .build(),

                    AddressDto.builder()
                            .street("First Ave")
                            .number("456")
                            .floor("3")
                            .door("B")
                            .postalCode("28002")
                            .city("Madrid")
                            .build()
            };
            List<Address> addresses = Arrays.stream(addressesDtos)
                    .map(AddressDto::toAddress)
                    .toList();
            addresses.getFirst().setUser(users.getFirst());
            addresses.get(1).setUser(users.get(1));
            this.addressRepository.saveAll(addresses);

            LogManager.getLogger(this.getClass()).warn("------- Products Initial Load -----------");
            ProductDto[] productDtos = {
                    ProductDto.builder()
                            .name("Golden Apple")
                            .barcode("1112223334445")
                            .price(BigDecimal.valueOf(2.99))
                            .quantity(100)
                            .category(Category.FRUITS)
                            .description("Fresh and juicy golden apple, rich in vitamins")
                            .build(),

                    ProductDto.builder()
                            .name("Cheddar Cheese")
                            .barcode("5556667778889")
                            .price(BigDecimal.valueOf(5.49))
                            .quantity(30)
                            .category(Category.DAIRY)
                            .description("Aged cheddar cheese with rich and creamy flavor")
                            .build(),
                    ProductDto.builder()
                            .name("Whole Bread")
                            .barcode("7778889990001")
                            .price(BigDecimal.valueOf(3.49))
                            .quantity(50)
                            .category(Category.BAKERY)
                            .description("Freshly baked whole wheat bread, high in fiber and nutrients")
                            .build(),
                    ProductDto.builder()
                            .name("Normal Bread")
                            .barcode("7778889990000")
                            .price(BigDecimal.valueOf(3.49))
                            .quantity(50)
                            .category(Category.BAKERY)
                            .description("Freshly baked wheat bread, high in fiber and nutrients")
                            .build()
            };

            List<Product> products = Arrays.stream(productDtos)
                    .map(ProductDto::toProduct)
                    .toList();
            this.productRepository.saveAll(products);
        }
    }

    public void deleteAll() {
        this.userRepository.deleteAll();
    }

}

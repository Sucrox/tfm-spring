package es.upm.miw.companyds.tfm_spring.persistence;

import es.upm.miw.companyds.tfm_spring.api.dto.AddressDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Address;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import es.upm.miw.companyds.tfm_spring.persistence.repository.AddressRepository;
import es.upm.miw.companyds.tfm_spring.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserSeederService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;


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

        }
    }

    public void deleteAll() {
        this.userRepository.deleteAll();
    }

}

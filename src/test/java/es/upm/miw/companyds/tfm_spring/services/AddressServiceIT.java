package es.upm.miw.companyds.tfm_spring.services;

import es.upm.miw.companyds.tfm_spring.TestConfig;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateAddressDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Address;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import es.upm.miw.companyds.tfm_spring.persistence.repository.AddressRepository;
import es.upm.miw.companyds.tfm_spring.persistence.repository.UserRepository;
import es.upm.miw.companyds.tfm_spring.services.exceptions.ForbiddenException;
import es.upm.miw.companyds.tfm_spring.services.exceptions.NotFoundException;
import es.upm.miw.companyds.tfm_spring.services.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestConfig
public class AddressServiceIT {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressServiceImpl addressService;

    /**
     * Configura un usuario autenticado en el contexto de seguridad de Spring antes de cada test.
     *
     */
    @BeforeEach
    void setupAuthenticatedUser() {
        User user = userRepository.findByPhone("666111222")
                .orElseThrow(() -> new RuntimeException("Test user not found"));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getPhone(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testUpdateAddress() {
        assertTrue(this.userRepository.findByPhone("666111222").isPresent());
        User user = this.userRepository.findByPhone("666111222").get();
        UpdateAddressDto updateAddressDto = UpdateAddressDto.builder().floor("5").build();

        assertEquals(updateAddressDto.getFloor(), addressService.updateAddress(user.getId(),updateAddressDto, Role.ADMIN).getFloor());
    }

    @Test
    void testUpdateAddressExceptions() {

        Address address= this.addressRepository.findAll().get(1);
        UpdateAddressDto updateAddressDto = UpdateAddressDto.builder().floor("5").build();

        assertThrows(ForbiddenException.class, () -> addressService.updateAddress(-1,updateAddressDto, Role.ADMIN));
        assertThrows(ForbiddenException.class, () -> addressService.updateAddress(address.getId(), updateAddressDto, Role.CUSTOMER));
    }

}

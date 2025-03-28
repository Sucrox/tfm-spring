package es.upm.miw.companyds.tfm_spring.api.controller;

import es.upm.miw.companyds.tfm_spring.api.dto.AddressDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateAddressDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.services.impl.AddressServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping(AddressController.ADDRESS)
public class AddressController {

    public static final String ADDRESS = "/address";

    private final AddressServiceImpl addressService;
    public static final String USER_ID = "/{user_id}";
    public static final String ADDRESS_ID = "/{id}";

    @Autowired
    public AddressController(AddressServiceImpl addressService) {
        this.addressService = addressService;
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("authenticated")
    @GetMapping(USER_ID)
    public ResponseEntity<Stream<AddressDto>> getAddressesByUserId(@PathVariable Integer user_id) {
        return ResponseEntity.ok(this.addressService.getAddressesByUserId(user_id,this.extractRoleClaims()));
    }


    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("authenticated")
    @PatchMapping(ADDRESS_ID)
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Integer id, @RequestBody UpdateAddressDto updateAddressDto) {
        return ResponseEntity.ok(this.addressService.updateAddress(id, updateAddressDto, this.extractRoleClaims()));
    }

    private Role extractRoleClaims() {
        List<String> roleClaims = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        return Role.of(roleClaims.getFirst());
    }
}

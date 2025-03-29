package es.upm.miw.companyds.tfm_spring.api.controller;

import es.upm.miw.companyds.tfm_spring.api.dto.AddressDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateAddressDto;

import es.upm.miw.companyds.tfm_spring.services.impl.AddressServiceImpl;
import es.upm.miw.companyds.tfm_spring.services.impl.AuthorizationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping(AddressController.ADDRESS)
public class AddressController {

    public static final String ADDRESS = "/address";
    public static final String USER = "/user";
    public static final String USER_ID = "/{user_id}";
    public static final String ADDRESS_ID = "/{id}";

    private final AddressServiceImpl addressService;
    private final AuthorizationService authorizationService;

    @Autowired
    public AddressController(AddressServiceImpl addressService,AuthorizationService authorizationService) {
        this.addressService = addressService;
        this.authorizationService = authorizationService;
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("authenticated")
    @GetMapping(USER+USER_ID)
    public ResponseEntity<Stream<AddressDto>> getAddressesByUserId(@PathVariable Integer user_id) {
        return ResponseEntity.ok(this.addressService.getAddressesByUserId(user_id,this.authorizationService.extractRoleClaims()));
    }


    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("authenticated")
    @PatchMapping(ADDRESS_ID)
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Integer id, @RequestBody UpdateAddressDto updateAddressDto) {
        return ResponseEntity.ok(this.addressService.updateAddress(id, updateAddressDto, this.authorizationService.extractRoleClaims()));
    }


}

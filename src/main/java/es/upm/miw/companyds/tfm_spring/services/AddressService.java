package es.upm.miw.companyds.tfm_spring.services;

import es.upm.miw.companyds.tfm_spring.api.dto.AddressDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateAddressDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;

import java.util.stream.Stream;

public interface AddressService {

    Stream<AddressDto> getAddressesByUserId(Integer userId, Role role);
    AddressDto updateAddress(Integer id, UpdateAddressDto updateAddressDto, Role role);

}

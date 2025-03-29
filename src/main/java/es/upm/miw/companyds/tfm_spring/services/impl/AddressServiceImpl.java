package es.upm.miw.companyds.tfm_spring.services.impl;

import es.upm.miw.companyds.tfm_spring.api.dto.AddressDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateAddressDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Address;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.repository.AddressRepository;
import es.upm.miw.companyds.tfm_spring.services.AddressService;
import es.upm.miw.companyds.tfm_spring.services.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final AuthorizationService authorizationService;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, AuthorizationService authorizationService) {
        this.addressRepository = addressRepository;
        this.authorizationService = authorizationService;
    }

    @Override
    public Stream<AddressDto> getAddressesByUserId(Integer userId, Role role) {
        this.authorizationService.checkIfAuthorized(role,userId);
        return addressRepository.findAllByUserId(userId).stream().map(AddressDto::ofAddress);
    }

    @Override
    public AddressDto updateAddress(Integer id, UpdateAddressDto updateAddressDto, Role role) {
        this.authorizationService.checkIfAuthorized(role,id);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address does not exist"));

        if (updateAddressDto.getStreet() != null && !updateAddressDto.getStreet().isEmpty()) {
            address.setStreet(updateAddressDto.getStreet());
        }

        if (updateAddressDto.getNumber() != null && !updateAddressDto.getNumber().isEmpty()) {
            address.setNumber(updateAddressDto.getNumber());
        }

        if (updateAddressDto.getFloor() != null && !updateAddressDto.getFloor().isEmpty()) {
            address.setFloor(updateAddressDto.getFloor());
        }

        if (updateAddressDto.getDoor() != null && !updateAddressDto.getDoor().isEmpty()) {
            address.setDoor(updateAddressDto.getDoor());
        }

        if (updateAddressDto.getPostalCode() != null && !updateAddressDto.getPostalCode().isEmpty()) {
            address.setPostalCode(updateAddressDto.getPostalCode());
        }

        if (updateAddressDto.getCity() != null && !updateAddressDto.getCity().isEmpty()) {
            address.setCity(updateAddressDto.getCity());
        }

        return AddressDto.ofAddress(this.addressRepository.save(address));
    }
}

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

import java.util.Optional;
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

        Optional.ofNullable(updateAddressDto.getStreet()).filter(s -> !s.isEmpty()).ifPresent(address::setStreet);
        Optional.ofNullable(updateAddressDto.getNumber()).filter(s -> !s.isEmpty()).ifPresent(address::setNumber);
        Optional.ofNullable(updateAddressDto.getFloor()).filter(s -> !s.isEmpty()).ifPresent(address::setFloor);
        Optional.ofNullable(updateAddressDto.getDoor()).filter(s -> !s.isEmpty()).ifPresent(address::setDoor);
        Optional.ofNullable(updateAddressDto.getPostalCode()).filter(s -> !s.isEmpty()).ifPresent(address::setPostalCode);
        Optional.ofNullable(updateAddressDto.getCity()).filter(s -> !s.isEmpty()).ifPresent(address::setCity);

        return AddressDto.ofAddress(this.addressRepository.save(address));
    }
}

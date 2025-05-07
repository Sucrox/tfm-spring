package es.upm.miw.companyds.tfm_spring.services.impl;

import es.upm.miw.companyds.tfm_spring.api.dto.AddressDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateAddressDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Address;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.repository.AddressRepository;
import es.upm.miw.companyds.tfm_spring.services.AddressService;
import es.upm.miw.companyds.tfm_spring.services.exceptions.ForbiddenException;
import jakarta.transaction.Transactional;
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
    public Stream<AddressDto> getAddressesByUserPhone(String userPhone, Role role) {
        this.authorizationService.checkIfAuthorized(role,userPhone);
        return addressRepository.findAllByUserPhone(userPhone).stream().map(AddressDto::ofAddress);
    }

    @Transactional
    @Override
    public AddressDto updateAddress(Integer id, UpdateAddressDto updateAddressDto, Role role) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new ForbiddenException("You are not allowed to make this call"));

        this.authorizationService.checkIfAuthorized(role, address.getUser().getPhone());

        Optional.ofNullable(updateAddressDto.getStreet()).filter(s -> !s.isEmpty()).ifPresent(address::setStreet);
        Optional.ofNullable(updateAddressDto.getNumber()).filter(s -> !s.isEmpty()).ifPresent(address::setNumber);
        Optional.ofNullable(updateAddressDto.getFloor()).filter(s -> !s.isEmpty()).ifPresent(address::setFloor);
        Optional.ofNullable(updateAddressDto.getDoor()).filter(s -> !s.isEmpty()).ifPresent(address::setDoor);
        Optional.ofNullable(updateAddressDto.getPostalCode()).filter(s -> !s.isEmpty()).ifPresent(address::setPostalCode);
        Optional.ofNullable(updateAddressDto.getCity()).filter(s -> !s.isEmpty()).ifPresent(address::setCity);

        return AddressDto.ofAddress(this.addressRepository.save(address));
    }
}

package es.upm.miw.companyds.tfm_spring.services.impl;

import es.upm.miw.companyds.tfm_spring.api.dto.AddressDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import es.upm.miw.companyds.tfm_spring.persistence.repository.AddressRepository;
import es.upm.miw.companyds.tfm_spring.persistence.repository.UserRepository;
import es.upm.miw.companyds.tfm_spring.services.AddressService;
import es.upm.miw.companyds.tfm_spring.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

@Service
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Stream<AddressDto> getAddressesByUserId(Integer userId, Role role) {
        this.checkIfAuthorized(role,userId);
        return addressRepository.findAllByUserId(userId).stream().map(AddressDto::ofAddress);
    }

    private void checkIfAuthorized(Role role, Integer id)  throws  ForbiddenException{
        if(!Objects.equals(id, this.extractUserID()) && !role.equals(Role.ADMIN)) {
            throw new ForbiddenException("You are not allowed to make this call");
        }
    }
    private Integer extractUserID() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }
        String phone = authentication.getName();
        return userRepository.findByPhone(phone).map(User::getId)
                .orElseThrow(() -> new ForbiddenException("Authenticated user not found"));
    }
}

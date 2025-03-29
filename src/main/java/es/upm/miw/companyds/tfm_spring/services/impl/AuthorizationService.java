package es.upm.miw.companyds.tfm_spring.services.impl;

import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import es.upm.miw.companyds.tfm_spring.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import es.upm.miw.companyds.tfm_spring.persistence.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AuthorizationService {

    private final UserRepository userRepository;

    @Autowired
    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    protected void checkIfAuthorized(Role role)  throws ForbiddenException {
        if (role != Role.ADMIN) {
            throw new ForbiddenException("Unauthorized operation");
        }
    }

    protected void checkIfAuthorized(Role role, Integer id)  throws  ForbiddenException{
        if(!Objects.equals(id, this.extractUserID()) && !role.equals(Role.ADMIN)) {
            throw new ForbiddenException("You are not allowed to make this call");
        }
    }

    protected Integer extractUserID() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }
        String phone = authentication.getName();
        return userRepository.findByPhone(phone).map(User::getId)
                .orElseThrow(() -> new ForbiddenException("Authenticated user not found"));
    }

    public Role extractRoleClaims() {
        List<String> roleClaims = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        return Role.of(roleClaims.getFirst());
    }
}

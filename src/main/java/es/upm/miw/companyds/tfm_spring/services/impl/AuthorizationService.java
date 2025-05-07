package es.upm.miw.companyds.tfm_spring.services.impl;

import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.services.exceptions.ForbiddenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AuthorizationService {

    protected void checkIfAuthorized(Role role)  throws ForbiddenException {
        if (role != Role.ADMIN) {
            throw new ForbiddenException("Unauthorized operation");
        }
    }

    protected void checkIfAuthorized(Role role, String phone) throws ForbiddenException {
        if (!Objects.equals(phone, this.extractUserPhone()) && !role.equals(Role.ADMIN)) {
            throw new ForbiddenException("You are not allowed to make this call");
        }
    }

    protected String extractUserPhone() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }
        return authentication.getName();
    }

    public Role extractRoleClaims() {
        List<String> roleClaims = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        return Role.of(roleClaims.getFirst());
    }
}

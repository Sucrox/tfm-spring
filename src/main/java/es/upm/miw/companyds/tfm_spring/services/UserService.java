package es.upm.miw.companyds.tfm_spring.services;

import es.upm.miw.companyds.tfm_spring.api.dto.LoginDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import jakarta.validation.Valid;

public interface UserService {

    void registerUser(@Valid UserDto user);

    String login(LoginDto loginDto);

}

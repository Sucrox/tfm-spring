package es.upm.miw.companyds.tfm_spring.services;

import es.upm.miw.companyds.tfm_spring.api.dto.LoginDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateUserDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import jakarta.validation.Valid;

import java.util.stream.Stream;

public interface UserService {

    void registerUser(@Valid UserDto user);

    String login(LoginDto loginDto);

    Stream<UserDto> getAllUsers(Role role);

    UserDto getUserByPhone(String phone, Role role);

    UserDto createUser(UserDto user, Role role);

    UserDto updateUserByPhone(String phone, UpdateUserDto updateUserDto, Role role);

    void deleteUserByPhone(String phone, Role role);
}

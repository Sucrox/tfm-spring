package es.upm.miw.companyds.tfm_spring.services.impl;

import es.upm.miw.companyds.tfm_spring.api.dto.LoginDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateUserDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import es.upm.miw.companyds.tfm_spring.persistence.repository.UserRepository;
import es.upm.miw.companyds.tfm_spring.services.UserService;
import es.upm.miw.companyds.tfm_spring.services.exceptions.ConflictException;
import es.upm.miw.companyds.tfm_spring.services.exceptions.ForbiddenException;
import es.upm.miw.companyds.tfm_spring.services.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

import static es.upm.miw.companyds.tfm_spring.api.dto.validation.Validations.isValidEmail;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthorizationService authorizationService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorizationService authorizationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
    }

    @Override
    @Transactional
    public void registerUser(UserDto userDto) {
        try {
            User user = userDto.toUser();
            userRepository.save(user);
        } catch (DataAccessException exception) {
            throw new ConflictException("Error saving User" + userDto.getEmail());
        } catch (Exception e) {
            throw new ForbiddenException("Unexpected error while creating User" + userDto.getEmail());
        }
    }

    @Override
    public UserDto login(LoginDto loginDto) throws ConflictException {
        User user =  this.userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new NotFoundException("Unauthorized login"));
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ForbiddenException("Incorrect fields");
        }
        return  UserDto.ofUser(user);
    }

    @Override
    public Stream<UserDto> getAllUsers(Role role) {
        if (role.equals(Role.ADMIN)) {
            return userRepository.findAll().stream().map(UserDto::ofUser);
        } else {
            throw new ForbiddenException("You are not allowed to make this call");
        }
    }

    @Override
    public UserDto getUserByPhone(String phone, Role role) {
        this.authorizationService.checkIfAuthorized(role,phone);
        return userRepository.findByPhone(phone).map(UserDto::ofUser)
                .orElseThrow(() -> new NotFoundException("There's no user for phone:" + phone));
    }


    @Transactional
    @Override
    public UserDto createUser(UserDto userDto, Role role) {
        this.authorizationService.checkIfAuthorized(role);
        if (userRepository.findByPhone(userDto.getPhone()).isPresent()) {
            throw new ConflictException("User already exists");
        }
        return UserDto.ofUser(this.userRepository.save(userDto.toUser()));
    }

    @Transactional
    @Override
    public UserDto updateUserByPhone(String phone, UpdateUserDto updateUserDto, Role role) {
        this.authorizationService.checkIfAuthorized(role,phone);
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException("User does not exist"));

        Optional.ofNullable(updateUserDto.getFirstName()).filter(name -> !name.isEmpty()).ifPresent(user::setFirstName);
        Optional.ofNullable(updateUserDto.getFamilyName()).filter(name -> !name.isEmpty()).ifPresent(user::setFamilyName);

        Optional.ofNullable(updateUserDto.getEmail())
                .filter(email -> !email.isEmpty())
                .ifPresent(email -> {
                    if (isValidEmail(email)) {
                        user.setEmail(email);
                    } else {
                        throw new ConflictException("Provided email is not valid");
                    }
                });
        return UserDto.ofUser(this.userRepository.save(user));
    }

    @Transactional
    @Override
    public void deleteUserByPhone(String phone, Role role) {
        this.authorizationService.checkIfAuthorized(role);
        userRepository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        userRepository.deleteByPhone(phone);
    }
}

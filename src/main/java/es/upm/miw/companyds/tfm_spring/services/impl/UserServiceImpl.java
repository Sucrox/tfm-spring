package es.upm.miw.companyds.tfm_spring.services.impl;

import es.upm.miw.companyds.tfm_spring.api.dto.LoginDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import es.upm.miw.companyds.tfm_spring.persistence.repository.UserRepository;
import es.upm.miw.companyds.tfm_spring.services.JwtService;
import es.upm.miw.companyds.tfm_spring.services.UserService;
import es.upm.miw.companyds.tfm_spring.services.exceptions.ConflictException;
import es.upm.miw.companyds.tfm_spring.services.exceptions.ForbiddenException;
import es.upm.miw.companyds.tfm_spring.services.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void registerUser(UserDto userDto) {
        try {
            if (userDto.getRole() == null) {
                userDto.setRole(Role.CUSTOMER);
            }
            User user = userDto.toUser();
            userRepository.save(user);
        } catch (DataAccessException exception) {
            throw new ConflictException("Error saving User" + userDto.getEmail());
        } catch (Exception e) {
            throw new ForbiddenException("Unexpected error while creating User" + userDto.getEmail());
        }
    }

    @Override
    public String login(LoginDto loginDto) throws ConflictException {
        User user =  this.userRepository.findByEmail(loginDto.getEmail())
                    .orElseThrow(() -> new NotFoundException("Unauthorized login"));
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ForbiddenException("Incorrect fields");
        }
        return  jwtService.createToken(user.getPhone(), user.getFirstName(), user.getRole().name());
    }

}

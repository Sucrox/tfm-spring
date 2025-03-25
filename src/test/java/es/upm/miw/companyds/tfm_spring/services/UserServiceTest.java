package es.upm.miw.companyds.tfm_spring.services;

import es.upm.miw.companyds.tfm_spring.TestConfig;
import es.upm.miw.companyds.tfm_spring.api.dto.LoginDto;
import es.upm.miw.companyds.tfm_spring.api.dto.TokenDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import es.upm.miw.companyds.tfm_spring.persistence.repository.UserRepository;
import es.upm.miw.companyds.tfm_spring.services.exceptions.ForbiddenException;
import es.upm.miw.companyds.tfm_spring.services.exceptions.NotFoundException;
import es.upm.miw.companyds.tfm_spring.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    /**
     * Configura un usuario autenticado en el contexto de seguridad de Spring antes de cada test.
     *
     */
    @BeforeEach
    void setupAuthenticatedUser() {
        User user = userRepository.findByPhone("666111222")
                .orElseThrow(() -> new RuntimeException("Test user not found"));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getPhone(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testRegisterUser() {
        UserDto userDto = UserDto.builder()
                .phone("666000666")
                .firstName("Juan")
                .familyName("Pérez")
                .dni("12121212K")
                .email("juan@example.com")
                .password("password123")
                .build();

        userService.registerUser(userDto);

        Optional<User> savedUserOpt = userRepository.findByPhone("666000666");
        assertTrue(savedUserOpt.isPresent());
        User savedUser = savedUserOpt.get();
        assertEquals("666000666", savedUser.getPhone());
        assertEquals(Role.CUSTOMER, savedUser.getRole());
    }

    @Test
    void testLoginUserSuccessful() {
        LoginDto loginDto = LoginDto.builder().email("juan.perez@example.com")
                .password("password123").build();

        TokenDto token= new TokenDto(userService.login(loginDto));
        assertNotNull(token);
    }
    @Test
    void testLoginUser_403Forbidden() {
        LoginDto loginDto = LoginDto.builder().email("juan.perez@example.com")
                .password("wrongpassword").build();
        Exception exception = assertThrows(ForbiddenException.class, () -> {
            userService.login(loginDto);
        });
    }
    @Test
    void testLoginUser_404NotFound() {
        LoginDto loginDto = LoginDto.builder().email("notFound@example.com")
                .password("wrongpassword").build();
        Exception exception = assertThrows(NotFoundException.class, () -> {
            userService.login(loginDto);
        });
    }
    @Test
    void testGetUserCorrectId() {
        User user = userRepository.findByPhone("666111222")
                .orElseThrow(() -> new RuntimeException("Test user not found"));
        UserDto userDto = userService.getUserById(user.getId(), user.getRole());

        assertNotNull(userDto);
        assertEquals("666111222", userDto.getPhone());
        assertEquals(Role.ADMIN, userDto.getRole());
    }

    @Test
    void testGetUserWrongId() {
        assertThrows(NotFoundException.class, () -> userService.getUserById(-1, Role.ADMIN));
    }

    @Test
    void testGetUsers() {
        Stream<UserDto> usersStream = userService.getAllUsers(Role.ADMIN);
        List<UserDto> usersList = usersStream.toList();
        assertTrue(usersList.size() > 1);
    }

    @Test
    void testGetUsersForbidden() {
        assertThrows(ForbiddenException.class, () -> userService.getAllUsers(Role.CUSTOMER));

    }


}

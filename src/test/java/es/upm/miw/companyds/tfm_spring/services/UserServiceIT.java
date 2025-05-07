package es.upm.miw.companyds.tfm_spring.services;

import es.upm.miw.companyds.tfm_spring.TestConfig;
import es.upm.miw.companyds.tfm_spring.api.dto.LoginDto;
import es.upm.miw.companyds.tfm_spring.api.dto.TokenDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateUserDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.persistence.model.User;
import es.upm.miw.companyds.tfm_spring.persistence.repository.UserRepository;
import es.upm.miw.companyds.tfm_spring.services.exceptions.ConflictException;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class UserServiceIT {

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

        UserDto savedUser = userService.getUserByPhone("666000666", Role.ADMIN);
        assertNotNull(savedUser);
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
        assertThrows(ForbiddenException.class, () -> userService.login(loginDto));
    }
    @Test
    void testLoginUser_404NotFound() {
        LoginDto loginDto = LoginDto.builder().email("notFound@example.com")
                .password("wrongpassword").build();
        assertThrows(NotFoundException.class, () -> userService.login(loginDto));
    }
    @Test
    void testGetUserCorrectPhone() {
        UserDto userDto = userService.getUserByPhone("666111222",Role.ADMIN);

        assertNotNull(userDto);
        assertEquals("666111222", userDto.getPhone());
        assertEquals(Role.ADMIN, userDto.getRole());
    }

    @Test
    void testGetUserWrongPhone() {
        assertThrows(NotFoundException.class, () -> userService.getUserByPhone("-1", Role.ADMIN));
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

    @Test
    void testCreateUser() {
        UserDto userDto =  UserDto.builder()
                .phone("677711222")
                .firstName("Juan")
                .familyName("Perez")
                .email("new@example.com")
                .dni("77345678A")
                .password("password123")
                .build();
        assertEquals(userDto.getPhone(), this.userService.createUser(userDto,Role.ADMIN).getPhone());
    }

    @Test
    void testCreateUserExceptions() {
        UserDto userDto =  UserDto.builder()
                .phone("666111222")
                .firstName("Juan")
                .familyName("Perez")
                .email("juan.perez@example.com")
                .dni("12345678A")
                .password("password123")
                .role(Role.ADMIN)
                .build();
        assertThrows(ForbiddenException.class, () -> userService.createUser(userDto, Role.CUSTOMER));
        assertThrows(ConflictException.class, () -> userService.createUser(userDto, Role.ADMIN));
    }

    @Test
    void testUpdateUser() {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .firstName("Juan")
                .familyName("Perez")
                .email("newEmail@example.com")
                .build();
        assertEquals(updateUserDto.getEmail(), userService.updateUserByPhone("616333625",updateUserDto, Role.ADMIN).getEmail());
    }

    @Test
    void testUpdateUserExceptions() {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .firstName("Juan")
                .familyName("Perez")
                .email("lalala")
                .build();
        assertThrows(NotFoundException.class, () -> userService.updateUserByPhone("-1",updateUserDto, Role.ADMIN));
        assertThrows(ConflictException.class, () -> userService.updateUserByPhone("616333625",updateUserDto, Role.ADMIN));
        assertThrows(ForbiddenException.class, () -> userService.updateUserByPhone("616333625", updateUserDto, Role.CUSTOMER));
    }

    @Test
    void testDeleteUser() {
        userService.deleteUserByPhone("616333999", Role.ADMIN);
        assertThrows(NotFoundException.class, () -> userService.deleteUserByPhone("616333999", Role.ADMIN));
    }

    @Test
    void testDeleteUserExceptions() {
        assertThrows(NotFoundException.class, () -> userService.deleteUserByPhone("-1", Role.ADMIN));
        assertThrows(ForbiddenException.class, () -> userService.deleteUserByPhone("-1", Role.CUSTOMER));
    }
}

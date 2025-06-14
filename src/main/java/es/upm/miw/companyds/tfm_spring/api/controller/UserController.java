package es.upm.miw.companyds.tfm_spring.api.controller;

import es.upm.miw.companyds.tfm_spring.api.dto.*;
import es.upm.miw.companyds.tfm_spring.services.JwtService;
import es.upm.miw.companyds.tfm_spring.services.impl.AuthorizationService;
import es.upm.miw.companyds.tfm_spring.services.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping(UserController.USERS)
public class UserController {

    public static final String USERS = "/users";
    public static final String LOGIN = "/login";
    public static final String USER_PHONE = "/{phone}";

    private final UserServiceImpl userService;
    private final AuthorizationService authorizationService;
    private final JwtService jwtService;


    @Autowired
    public UserController(UserServiceImpl userService, AuthorizationService authorizationService, JwtService jwtService) {
        this.userService = userService;
        this.authorizationService = authorizationService;
        this.jwtService = jwtService;

    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        userService.registerUser(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "basicAuth")
    @PostMapping(value = LOGIN)
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginDto loginDto) {
        UserDto user = userService.login(loginDto);
        String token = jwtService.createToken(user.getPhone(), user.getFirstName(), user.getRole().name());
        return ResponseEntity.ok(new LoginResponseDto(token, user.getPhone()));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("authenticated")
    @GetMapping
    public ResponseEntity<Stream<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers(this.authorizationService.extractRoleClaims()));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("authenticated")
    @GetMapping(USER_PHONE)
    public ResponseEntity<UserDto> getUser(@PathVariable String phone) {
        return ResponseEntity.ok(this.userService.getUserByPhone(phone, this.authorizationService.extractRoleClaims()));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("authenticated")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto response = this.userService.createUser(userDto, this.authorizationService.extractRoleClaims());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("authenticated")
    @PatchMapping(USER_PHONE)
    public ResponseEntity<UserDto> updateUser(@PathVariable String phone, @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(this.userService.updateUserByPhone(phone, updateUserDto, this.authorizationService.extractRoleClaims()));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("authenticated")
    @DeleteMapping(USER_PHONE)
    public  ResponseEntity<Void> deleteUSer(@PathVariable String phone) {
        this.userService.deleteUserByPhone(phone, this.authorizationService.extractRoleClaims());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

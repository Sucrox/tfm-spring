package es.upm.miw.companyds.tfm_spring.api.controller;

import es.upm.miw.companyds.tfm_spring.api.dto.LoginDto;
import es.upm.miw.companyds.tfm_spring.api.dto.TokenDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateUserDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
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
    public static final String USER_ID = "/{id}";

    private final UserServiceImpl userService;
    private final AuthorizationService authorizationService;

    @Autowired
    public UserController(UserServiceImpl userService, AuthorizationService authorizationService) {
        this.userService = userService;
        this.authorizationService = authorizationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        userService.registerUser(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "basicAuth")
    @PostMapping(value = LOGIN)
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        TokenDto token = new TokenDto(userService.login(loginDto));
        return ResponseEntity.ok(token);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("authenticated")
    @GetMapping
    public ResponseEntity<Stream<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers(this.authorizationService.extractRoleClaims()));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("authenticated")
    @GetMapping(USER_ID)
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(this.userService.getUserById(id, this.authorizationService.extractRoleClaims()));
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
    @PatchMapping(USER_ID)
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer id, @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(this.userService.updateUser(id, updateUserDto, this.authorizationService.extractRoleClaims()));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("authenticated")
    @DeleteMapping(USER_ID)
    public  ResponseEntity<Void> deleteUSer(@PathVariable Integer id) {
        this.userService.deleteUser(id, this.authorizationService.extractRoleClaims());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

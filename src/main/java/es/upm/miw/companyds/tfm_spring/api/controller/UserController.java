package es.upm.miw.companyds.tfm_spring.api.controller;

import es.upm.miw.companyds.tfm_spring.api.dto.LoginDto;
import es.upm.miw.companyds.tfm_spring.api.dto.TokenDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UpdateUserDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.persistence.model.Role;
import es.upm.miw.companyds.tfm_spring.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping(UserController.USERS)
public class UserController {

    public static final String USERS = "/users";
    public static final String LOGIN = "/login";
    public static final String USER_ID = "/{id}";

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        userService.registerUser(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = LOGIN)
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        TokenDto token = new TokenDto(userService.login(loginDto));
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping
    public Stream<UserDto> getAllUsers() {
        return this.userService.getAllUsers(this.extractRoleClaims());
    }

    @GetMapping(USER_ID)
    public UserDto getUser(@PathVariable Integer id) {
        return this.userService.getUserById(id,this.extractRoleClaims());
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        return this.userService.createUser(userDto, this.extractRoleClaims());
    }

    @PatchMapping(USER_ID)
    public UserDto updateUser(@PathVariable Integer id, @RequestBody UpdateUserDto updateUserDto) {
        return this.userService.updateUser(id,updateUserDto, this.extractRoleClaims());
    }

    @DeleteMapping(USER_ID)
    public  ResponseEntity<Void> deleteUSer(@PathVariable Integer id) {
        this.userService.deleteUser(id, this.extractRoleClaims());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Role extractRoleClaims() {
        List<String> roleClaims = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        return Role.of(roleClaims.getFirst());
    }

}

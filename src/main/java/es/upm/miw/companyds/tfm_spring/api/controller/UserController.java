package es.upm.miw.companyds.tfm_spring.api.controller;

import es.upm.miw.companyds.tfm_spring.api.dto.LoginDto;
import es.upm.miw.companyds.tfm_spring.api.dto.TokenDto;
import es.upm.miw.companyds.tfm_spring.api.dto.UserDto;
import es.upm.miw.companyds.tfm_spring.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.USERS)
public class UserController {

    public static final String USERS = "/users";
    public static final String LOGIN = "/login";

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

}

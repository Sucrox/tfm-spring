package es.upm.miw.companyds.tfm_spring.api.controllers;

import es.upm.miw.companyds.tfm_spring.TestConfig;
import es.upm.miw.companyds.tfm_spring.api.controller.UserController;
import es.upm.miw.companyds.tfm_spring.services.UserServiceTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;


@TestConfig
public class UserControllerTest {

    @Mock
    private UserServiceTest userService;

    @InjectMocks
    private UserController userController;

}
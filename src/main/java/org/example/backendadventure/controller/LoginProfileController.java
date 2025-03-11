package org.example.backendadventure.controller;

import org.example.backendadventure.model.login.LoginProfile;
import org.example.backendadventure.service.loginservice.LoginProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin // This annotation allows any domain to access the endpoint. In our case on our localhost port 8080
@RestController
public class LoginProfileController {

    //***ATTRIBUTES***--------------------------------------------------------------------------------------------------
    private final LoginProfileService loginProfileService;

    //***CONSTRUCTOR***-------------------------------------------------------------------------------------------------
    public LoginProfileController(LoginProfileService loginProfileService) {
        this.loginProfileService = loginProfileService;
    }

    //***POST MAPPING METHODS***----------------------------------------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<Boolean> isAbleToLogin(@RequestBody LoginProfile loginProfile) {
        return new ResponseEntity<>(loginProfileService.isAbleToLogin(loginProfile), HttpStatus.OK);
    }

    //***END CLASS***---------------------------------------------------------------------------------------------------
}

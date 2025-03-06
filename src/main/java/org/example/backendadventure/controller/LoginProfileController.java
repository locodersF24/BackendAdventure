package org.example.backendadventure.controller;

import org.example.backendadventure.model.login.LoginProfile;
import org.example.backendadventure.service.loginservice.LoginProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("login")
public class LoginProfileController {

    //***ATTRIBUTES***--------------------------------------------------------------------------------------------------
    @Autowired
    LoginProfileService loginProfileService;

    //***CONSTRUCTOR***-------------------------------------------------------------------------------------------------
    public LoginProfileController(LoginProfileService loginProfileService) {
        this.loginProfileService = loginProfileService;
    }

    //***POST MAPPING METHODS***----------------------------------------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<Boolean> isAbleToLogin(@RequestBody LoginProfile loginProfile) {
        return loginProfileService.isAbleToLogin(loginProfile)
                ? ResponseEntity.ok(true)
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    //***END CLASS***---------------------------------------------------------------------------------------------------
}

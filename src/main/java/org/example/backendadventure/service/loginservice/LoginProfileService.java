package org.example.backendadventure.service.loginservice;

import org.example.backendadventure.model.login.LoginProfile;
import org.example.backendadventure.repository.loginrepo.LoginProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginProfileService {

    //***ATTRIBUTES***--------------------------------------------------------------------------------------------------
    private final LoginProfileRepository loginProfileRepository;

    //***CONSTRUCTOR***-------------------------------------------------------------------------------------------------
    public LoginProfileService(LoginProfileRepository loginProfileRepository) {
        this.loginProfileRepository = loginProfileRepository;
    }

    //***METHODS***-----------------------------------------------------------------------------------------------------
    public List<LoginProfile> getAllLoginProfiles(){
        return loginProfileRepository.findAll();
    }

    public boolean isAbleToLogin(LoginProfile loginProfile) {
        return getAllLoginProfiles().contains(loginProfile);
    }

    //***END CLASS***---------------------------------------------------------------------------------------------------
}

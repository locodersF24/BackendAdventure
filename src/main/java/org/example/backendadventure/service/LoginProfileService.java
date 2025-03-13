package org.example.backendadventure.service;

import org.example.backendadventure.model.LoginProfile;
import org.example.backendadventure.repository.LoginProfileRepository;
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

    //Checks if the attributes in profile has a match in the database
    public boolean isAbleToLogin(LoginProfile loginProfile) {
        return getAllLoginProfiles().stream()
                .anyMatch(profile ->
                        profile.getUsername().equalsIgnoreCase(loginProfile.getUsername()) &&
                        profile.getPassword().equals(loginProfile.getPassword()));
    }

    //***END CLASS***---------------------------------------------------------------------------------------------------
}

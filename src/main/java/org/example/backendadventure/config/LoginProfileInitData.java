package org.example.backendadventure.config;

import org.example.backendadventure.model.login.LoginProfile;
import org.example.backendadventure.repository.loginrepo.LoginProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginProfileInitData {

    //***ATTRIBUTES***--------------------------------------------------------------------------------------------------
    @Autowired
    private LoginProfileRepository loginProfileRepository;

    //***METHODS***-----------------------------------------------------------------------------------------------------
    public void loginProfilesInitData(){
        LoginProfile reservationManager = new LoginProfile();
        LoginProfile activityManager = new LoginProfile();

        reservationManager.setUsername("Reservation-Manager");
        reservationManager.setPassword("Password123");

        activityManager.setUsername("Activity-Manager");
        activityManager.setPassword("Password123");

        loginProfileRepository.save(reservationManager);
        loginProfileRepository.save(activityManager);
    }

    //***END CLASS***---------------------------------------------------------------------------------------------------
}

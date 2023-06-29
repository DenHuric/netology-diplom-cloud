package com.dhuric.projects.netology_diploma_cloud_backend.controllers;

import com.dhuric.projects.netology_diploma_cloud_backend.entities.UserCredentials;
import com.dhuric.projects.netology_diploma_cloud_backend.models.Login;
import com.dhuric.projects.netology_diploma_cloud_backend.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping("/login")
    public Login getAuthToken(@RequestBody UserCredentials userCredentials) {
        return authenticationService.validateUser(userCredentials.getLogin(), userCredentials.getPassword());
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Auth-Token") String auth_token) {
        authenticationService.removeAuthToken(auth_token);
    }
}


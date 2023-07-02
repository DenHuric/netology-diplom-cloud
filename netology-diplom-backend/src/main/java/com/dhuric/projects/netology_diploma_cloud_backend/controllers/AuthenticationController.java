package com.dhuric.projects.netology_diploma_cloud_backend.controllers;

import com.dhuric.projects.netology_diploma_cloud_backend.entities.UserCredentials;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.AuthenticationException;
import com.dhuric.projects.netology_diploma_cloud_backend.models.Login;
import com.dhuric.projects.netology_diploma_cloud_backend.repositories.UserCredentialsRepository;
import com.dhuric.projects.netology_diploma_cloud_backend.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    public Login checkUser(String login, String password) {
        try {
            UserCredentials user = userCredentialsRepository.findByLogin(login);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (user == null || !encoder.matches(password, user.getPassword())) {
                throw new AuthenticationException("Bad credentials");
            }
        } catch (Exception e) {
            throw new AuthenticationException("Bad credentials");
        }
        return authenticationService.validateUser(login);
    }


    @PostMapping("/login")
    public Login getAuthToken(@RequestBody UserCredentials userCredentials) {
        return checkUser(userCredentials.getLogin(), userCredentials.getPassword());
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Auth-Token") String auth_token) {
        authenticationService.removeAuthToken(auth_token);
    }
}


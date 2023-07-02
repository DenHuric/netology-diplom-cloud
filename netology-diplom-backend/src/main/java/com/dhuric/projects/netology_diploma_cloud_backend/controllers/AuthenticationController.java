package com.dhuric.projects.netology_diploma_cloud_backend.controllers;

import com.dhuric.projects.netology_diploma_cloud_backend.entities.UserCredentials;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.AuthenticationException;
import com.dhuric.projects.netology_diploma_cloud_backend.models.Login;
import com.dhuric.projects.netology_diploma_cloud_backend.repositories.UserCredentialsRepository;
import com.dhuric.projects.netology_diploma_cloud_backend.services.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public Login checkUser(String login, String password) {
        logger.info("Checking user: {}", login);
        try {
            UserCredentials user = userCredentialsRepository.findByLogin(login);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (user == null || !encoder.matches(password, user.getPassword())) {
                logger.info("Bad credentials");
                throw new AuthenticationException("Bad credentials");
            }
        } catch (Exception e) {
            logger.error("Error during user authentication: {}", e.getMessage());
            throw new AuthenticationException("Bad credentials");
        }
        return authenticationService.validateUser(login);
    }


    @PostMapping("/login")
    public Login getAuthToken(@RequestBody UserCredentials userCredentials) {
        logger.info("Received login request for user: {}", userCredentials.getLogin());
        return checkUser(userCredentials.getLogin(), userCredentials.getPassword());
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Auth-Token") String auth_token) {
        logger.info("Received logout request, token removed");
        authenticationService.removeAuthToken(auth_token);
    }
}


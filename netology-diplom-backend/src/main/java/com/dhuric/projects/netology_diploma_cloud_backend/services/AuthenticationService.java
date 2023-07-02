package com.dhuric.projects.netology_diploma_cloud_backend.services;

import com.dhuric.projects.netology_diploma_cloud_backend.entities.UserCredentials;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.TokenException;
import com.dhuric.projects.netology_diploma_cloud_backend.models.Login;
import com.dhuric.projects.netology_diploma_cloud_backend.repositories.UserCredentialsRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AuthenticationService {
    public AuthenticationService(UserCredentialsRepository userCredentialsRep) {
        userCredentialsRepository = userCredentialsRep;
    }

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private static final int TOKEN_SIZE = 32;

    public static String generateAuthToken() {
        byte[] tokenBytes = new byte[TOKEN_SIZE];
        new SecureRandom().nextBytes(tokenBytes);

        return Base64.getEncoder().encodeToString(tokenBytes);
    }

    public void removeAuthToken(String authToken) {
        userCredentialsRepository.deleteAuthTokenByAuthToken(authToken);
    }

    public Login validateUser(String login) {
        logger.info("Generating auth token for user: {}", login);
        String auth_token = generateAuthToken();
        userCredentialsRepository.addAuthToken(login, "Bearer " + auth_token);
        return new Login(auth_token);
    }

    public void checkUserAuthToken(String authToken) {
        logger.info("Checking auth token");
        UserCredentials user = userCredentialsRepository.findByAuthToken(authToken);
        if (user == null) {
            logger.warn("Unauthorized access with invalid auth token: {}", authToken);
            throw new TokenException("Unauthorized error");
        }
    }

    @PostConstruct
    public void createDefaultUsersCredentials() {
        if (userCredentialsRepository.count() == 0) {
            UserCredentials user = new UserCredentials();
            user.setLogin("user");
            user.setPassword("$2a$12$U4hy8uwsdlnZMzb32JjJBuxuocd.JUzYn9eokPoITwpnTUgLGUxZG");
            user.setAuthToken(null);
            userCredentialsRepository.save(user);
            UserCredentials testUser = new UserCredentials();
            testUser.setLogin("test");
            testUser.setPassword("$2a$12$VM13PzSOgaq/hzgHQKZ.Kuo1WWxoqGeN1l8t1J364hIVJqxx3dIju");
            testUser.setAuthToken(null);
            userCredentialsRepository.save(testUser);
        }
    }

}

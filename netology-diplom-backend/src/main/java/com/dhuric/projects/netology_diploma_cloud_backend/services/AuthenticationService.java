package com.dhuric.projects.netology_diploma_cloud_backend.services;

import com.dhuric.projects.netology_diploma_cloud_backend.entities.UserCredentials;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.AuthenticationException;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.TokenException;
import com.dhuric.projects.netology_diploma_cloud_backend.models.Login;
import com.dhuric.projects.netology_diploma_cloud_backend.repositories.UserCredentialsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AuthenticationService {
    public AuthenticationService(UserCredentialsRepository userCredentialsRep){
        userCredentialsRepository = userCredentialsRep;
    }
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;


    private static final int TOKEN_SIZE = 32;

    public static String generateAuthToken() {
        byte[] tokenBytes = new byte[TOKEN_SIZE];
        new SecureRandom().nextBytes(tokenBytes);

        return Base64.getEncoder().encodeToString(tokenBytes);
    }

    public void removeAuthToken(String authToken) {
        userCredentialsRepository.deleteAuthTokenByAuthToken(authToken);
    }

    public Login validateUser(String login, String password) {
        UserCredentials user = userCredentialsRepository.findByLogin(login);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Bad credentials");
        }
        String auth_token = generateAuthToken();
        userCredentialsRepository.addAuthToken(login, "Bearer " + auth_token);
        return new Login(auth_token);
    }

    public void checkUserAuthToken(String authToken) {
        UserCredentials user = userCredentialsRepository.findByAuthToken(authToken);
        if (user == null) {
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

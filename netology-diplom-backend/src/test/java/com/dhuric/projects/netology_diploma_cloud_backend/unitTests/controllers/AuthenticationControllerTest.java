package com.dhuric.projects.netology_diploma_cloud_backend.unitTests.controllers;

import com.dhuric.projects.netology_diploma_cloud_backend.controllers.AuthenticationController;
import com.dhuric.projects.netology_diploma_cloud_backend.entities.UserCredentials;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.AuthenticationException;
import com.dhuric.projects.netology_diploma_cloud_backend.models.Login;
import com.dhuric.projects.netology_diploma_cloud_backend.repositories.UserCredentialsRepository;
import com.dhuric.projects.netology_diploma_cloud_backend.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {
    @Mock
    private UserCredentialsRepository userCredentialsRepository;
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckUser_WithValidCredentials() {
        // Arrange
        String login = "test";
        String password = "password";
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setPassword(new BCryptPasswordEncoder().encode(password));

        when(userCredentialsRepository.findByLogin(eq(login))).thenReturn(userCredentials);
        when(authenticationService.validateUser(eq(login))).thenReturn(new Login("authToken"));

        // Act
        Login result = authenticationController.checkUser(login, password);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getAuthToken());
        verify(userCredentialsRepository, times(1)).findByLogin(eq(login));
        verify(authenticationService, times(1)).validateUser(eq(login));
    }

    @Test
    void testCheckUser_WithInvalidCredentials() {
        // Arrange
        String login = "test";
        String password = "invalidPassword";
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setPassword(new BCryptPasswordEncoder().encode("password")); // Store a different password

        when(userCredentialsRepository.findByLogin(eq(login))).thenReturn(userCredentials);

        // Act and Assert
        assertThrows(AuthenticationException.class, () -> {
            authenticationController.checkUser(login, password);
        });

        verify(userCredentialsRepository, times(1)).findByLogin(eq(login));
        verify(authenticationService, never()).validateUser(anyString());
    }
}
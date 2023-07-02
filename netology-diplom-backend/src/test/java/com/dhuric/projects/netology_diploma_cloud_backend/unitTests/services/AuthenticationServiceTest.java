package com.dhuric.projects.netology_diploma_cloud_backend.unitTests.services;

import com.dhuric.projects.netology_diploma_cloud_backend.entities.UserCredentials;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.AuthenticationException;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.TokenException;
import com.dhuric.projects.netology_diploma_cloud_backend.models.Login;
import com.dhuric.projects.netology_diploma_cloud_backend.repositories.UserCredentialsRepository;
import com.dhuric.projects.netology_diploma_cloud_backend.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateAuthToken() {
        // Act
        String authToken = AuthenticationService.generateAuthToken();

        // Assert
        assertNotNull(authToken);
        assertEquals(44, authToken.length());
    }

    @Test
    void testRemoveAuthToken() {
        // Arrange
        String authToken = "testAuthToken";

        // Act
        authenticationService.removeAuthToken(authToken);

        // Assert
        verify(userCredentialsRepository, times(1)).deleteAuthTokenByAuthToken(authToken);
    }

    @Test
    void testCheckUserAuthToken_WithValidToken() {
        // Arrange
        String authToken = "testAuthToken";
        UserCredentials userCredentials = new UserCredentials();

        when(userCredentialsRepository.findByAuthToken(eq(authToken))).thenReturn(userCredentials);

        // Act and Assert
        assertDoesNotThrow(() -> authenticationService.checkUserAuthToken(authToken));
        verify(userCredentialsRepository, times(1)).findByAuthToken(eq(authToken));
    }

    @Test
    void testCheckUserAuthToken_WithInvalidToken() {
        // Arrange
        String authToken = "invalidAuthToken";

        when(userCredentialsRepository.findByAuthToken(eq(authToken))).thenReturn(null);

        // Act and Assert
        assertThrows(TokenException.class, () -> authenticationService.checkUserAuthToken(authToken));
        verify(userCredentialsRepository, times(1)).findByAuthToken(eq(authToken));
    }
}

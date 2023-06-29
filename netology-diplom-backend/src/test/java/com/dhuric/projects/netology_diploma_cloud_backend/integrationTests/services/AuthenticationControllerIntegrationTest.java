package com.dhuric.projects.netology_diploma_cloud_backend.integrationTests.services;

import com.dhuric.projects.netology_diploma_cloud_backend.NetologyDiplomaCloudBackendApplication;
import com.dhuric.projects.netology_diploma_cloud_backend.entities.UserCredentials;
import com.dhuric.projects.netology_diploma_cloud_backend.models.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = NetologyDiplomaCloudBackendApplication.class)
public class AuthenticationControllerIntegrationTest {
    private String authToken;
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("cloud_authorization")
            .withUsername("root")
            .withPassword("root");

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }


    @Test
    public void testGetAuthToken_ValidUserCredentials_ReturnsAuthToken() {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setLogin("test");
        userCredentials.setPassword("test");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserCredentials> request = new HttpEntity<>(userCredentials, headers);

        Login response = restTemplate.exchange(
                "http://localhost:" + port + "/login",
                HttpMethod.POST,
                request,
                Login.class
        ).getBody();

        assert response != null;
        authToken = response.getAuthToken();
        assertNotNull(response);
        assertNotNull(response.getAuthToken());
    }

    @Test
    public void testLogout_ValidAuthToken_NoExceptionThrown() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Auth-Token", authToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/logout",
                HttpMethod.POST,
                request,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}


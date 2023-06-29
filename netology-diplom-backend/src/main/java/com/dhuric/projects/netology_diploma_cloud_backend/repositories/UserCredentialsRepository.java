package com.dhuric.projects.netology_diploma_cloud_backend.repositories;

import com.dhuric.projects.netology_diploma_cloud_backend.entities.UserCredentials;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
    UserCredentials findByLogin(String login);
    UserCredentials findByAuthToken(String authToken);

    @Transactional
    default void addAuthToken(String login, String authToken) {
        UserCredentials userCredentials = findByLogin(login);
        if (userCredentials != null) {
            userCredentials.setAuthToken(authToken);
        }
    }

    @Transactional
    default void deleteAuthTokenByAuthToken(String authToken) {
        UserCredentials userCredentials = findByAuthToken(authToken);
        if (userCredentials != null) {
            userCredentials.setAuthToken(null);
        }
    }

}

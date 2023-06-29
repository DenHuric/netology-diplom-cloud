package com.dhuric.projects.netology_diploma_cloud_backend.entities;

import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@Entity
public class UserCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String login;

    private String password;

    private String authToken;
}

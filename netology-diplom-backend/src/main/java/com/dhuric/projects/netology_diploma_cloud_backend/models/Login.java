package com.dhuric.projects.netology_diploma_cloud_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    @JsonProperty("auth-token")
    private String authToken;
}

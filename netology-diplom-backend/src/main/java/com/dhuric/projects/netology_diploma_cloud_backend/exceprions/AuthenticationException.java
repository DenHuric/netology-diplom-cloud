package com.dhuric.projects.netology_diploma_cloud_backend.exceprions;

import lombok.Data;

@Data
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String msg) {
        super(msg);
    }
}

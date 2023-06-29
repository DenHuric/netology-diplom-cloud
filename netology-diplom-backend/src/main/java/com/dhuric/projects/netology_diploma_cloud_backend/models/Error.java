package com.dhuric.projects.netology_diploma_cloud_backend.models;

import lombok.Data;

@Data
public class Error {
    private final String message;
    private final int id;
}

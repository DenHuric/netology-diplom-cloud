package com.dhuric.projects.netology_diploma_cloud_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDetails {
    private String filename;
    private int size;
}

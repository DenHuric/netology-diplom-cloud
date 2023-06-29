package com.dhuric.projects.netology_diploma_cloud_backend.controllers;

import com.dhuric.projects.netology_diploma_cloud_backend.dto.FileDetails;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.FileException;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.InputDataException;
import com.dhuric.projects.netology_diploma_cloud_backend.services.AuthenticationService;
import com.dhuric.projects.netology_diploma_cloud_backend.services.CloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

@RestController
public class CloudController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private CloudService cloudService;

    @PostMapping("/file")
    public void uploadFile(@RequestHeader("Auth-Token") String authToken,
                           @RequestBody MultipartFile file) throws InputDataException, IOException {
        cloudService.saveFile(file.getBytes(), file.getOriginalFilename(), authToken);
    }

    @DeleteMapping("/file")
    public void deleteFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String fileName
    ) throws FileException, InputDataException {
        cloudService.deleteFile(fileName, authToken);
    }


    @GetMapping("/file")
    public byte[] downloadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String fileName
    ) throws InputDataException, FileException {
        return cloudService.downloadFile(fileName, authToken);
    }

    @PutMapping("/file")
    public void editFileName(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String fileName,
            @RequestBody String newFileName
    ) throws FileException, InputDataException {
        cloudService.renameFile(fileName, newFileName,authToken);
    }

    @GetMapping("/list")
    public List<FileDetails> getFileList(
            @RequestHeader("auth-token") String authToken,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) throws FileException, InputDataException {
        return cloudService.listFiles(limit,authToken);
    }
}

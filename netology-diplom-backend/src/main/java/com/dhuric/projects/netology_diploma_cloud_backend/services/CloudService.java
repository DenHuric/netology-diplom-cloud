package com.dhuric.projects.netology_diploma_cloud_backend.services;

import com.dhuric.projects.netology_diploma_cloud_backend.dto.FileDetails;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.FileException;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.InputDataException;
import com.dhuric.projects.netology_diploma_cloud_backend.repositories.CloudRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
@Data
public class CloudService {
    @Autowired
    private CloudRepository cloudRepository;
    @Autowired
    private AuthenticationService authenticationService;

    public void checkInputFileName(String fileName) throws InputDataException {
        if (fileName == null) {
            throw new InputDataException("Error input data");
        }
    }

    public List<FileDetails> listFiles(int limit, String authToken) throws InputDataException, FileException {
        authenticationService.checkUserAuthToken(authToken);
        if (limit <= 0) {
            throw new InputDataException("Error input data");
        }
        return cloudRepository.getFileListFromStorage(limit);
    }

    public void saveFile(byte[] file, String fileName, String authToken) throws InputDataException {
        if (fileName == null) {
            throw new InputDataException("Error input data");
        }
        authenticationService.checkUserAuthToken(authToken);
        cloudRepository.saveFileToStorage(file, fileName);
    }

    public boolean deleteFile(String fileName, String authToken) throws FileException, InputDataException {
        if (fileName == null) throw new InputDataException("Error input data");
        authenticationService.checkUserAuthToken(authToken);
        return cloudRepository.deleteFileFromStorage(fileName);
    }

    public byte[] downloadFile(String fileName, String authToken) throws InputDataException, FileException {
        authenticationService.checkUserAuthToken(authToken);
        checkInputFileName(fileName);

        byte[] fileBytes;
        File file = cloudRepository.downloadFileFromRepository(fileName);
        try {
            fileBytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new FileException("Error upload file");
        }
        return fileBytes;
    }

    public void renameFile(String fileName, String newFileName, String authToken) throws InputDataException, FileException {
        authenticationService.checkUserAuthToken(authToken);
        checkInputFileName(fileName);
        checkInputFileName(newFileName);
        cloudRepository.renameFileOnRepository(fileName, newFileName);
    }

}

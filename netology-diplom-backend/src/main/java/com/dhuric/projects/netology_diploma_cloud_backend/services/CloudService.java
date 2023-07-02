package com.dhuric.projects.netology_diploma_cloud_backend.services;

import com.dhuric.projects.netology_diploma_cloud_backend.dto.FileDetails;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.FileException;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.InputDataException;
import com.dhuric.projects.netology_diploma_cloud_backend.repositories.CloudRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public void checkInputFileName(String fileName) throws InputDataException {
        if (fileName == null) {
            throw new InputDataException("Error input data");
        }
    }

    public List<FileDetails> listFiles(int limit, String authToken) throws InputDataException, FileException {
        try {
            logger.info("Getting file list with limit: {}", limit);
            authenticationService.checkUserAuthToken(authToken);
            if (limit <= 0) {
                throw new InputDataException("Error input data");
            }
            List<FileDetails> fileList = cloudRepository.getFileListFromStorage(limit);
            return fileList;
        } catch (InputDataException | FileException e) {
            logger.error("Error getting file list: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error getting file list", e);
            throw new FileException("Unexpected error getting file list");
        }
    }

    public void saveFile(byte[] file, String fileName, String authToken) throws InputDataException, FileException {
        try {
            logger.info("Saving file: {}", fileName);
            if (fileName == null) {
                throw new InputDataException("Error input data");
            }
            authenticationService.checkUserAuthToken(authToken);
            cloudRepository.saveFileToStorage(file, fileName);
        } catch (InputDataException e) {
            logger.error("Error saving file: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error saving file: {}", fileName, e);
            throw new FileException("Unexpected error saving file");
        }
    }

    public boolean deleteFile(String fileName, String authToken) throws FileException, InputDataException {
        try {
            logger.info("Deleting file: {}", fileName);
            if (fileName == null) throw new InputDataException("Error input data");
            authenticationService.checkUserAuthToken(authToken);
            boolean deleted = cloudRepository.deleteFileFromStorage(fileName);

            return deleted;
        } catch (InputDataException | FileException e) {
            logger.error("Error deleting file: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error deleting file: {}", fileName, e);
            throw new FileException("Unexpected error deleting file");
        }
    }

    public byte[] downloadFile(String fileName, String authToken) throws InputDataException, FileException {
        try {
            logger.info("Downloading file: {}", fileName);
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
        } catch (InputDataException | FileException e) {
            logger.error("Error downloading file: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error downloading file: {}", fileName, e);
            throw new FileException("Unexpected error downloading file");
        }
    }

    public void renameFile(String fileName, String newFileName, String authToken) throws InputDataException, FileException {
        try {
            logger.info("Renaming file: {} to {}", fileName, newFileName);
            authenticationService.checkUserAuthToken(authToken);
            checkInputFileName(fileName);
            checkInputFileName(newFileName);
            cloudRepository.renameFileOnRepository(fileName, newFileName);
        } catch (InputDataException | FileException e) {
            logger.error("Error renaming file: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error renaming file: {} to {}", fileName, newFileName, e);
            throw new FileException("Unexpected error renaming file");
        }
    }

}

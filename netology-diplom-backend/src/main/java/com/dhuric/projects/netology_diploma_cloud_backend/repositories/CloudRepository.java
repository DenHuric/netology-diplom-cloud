package com.dhuric.projects.netology_diploma_cloud_backend.repositories;

import com.dhuric.projects.netology_diploma_cloud_backend.dto.FileDetails;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.FileException;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.InputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CloudRepository {
    @Value("${cloud.storeDirectory}")
    private String directory_path;

    private static final Logger logger = LoggerFactory.getLogger(CloudRepository.class);

    public List<FileDetails> getFileListFromStorage(int limit) throws FileException {
        try {
            logger.info("Getting file list from storage");
            File directory = new File(directory_path);
            File[] files = directory.listFiles();

            List<FileDetails> fileList = new ArrayList<>();
            if (files != null) {
                int count = 0;
                for (File file : files) {
                    if (file.isFile()) {
                        FileDetails fileDetails = new FileDetails();
                        fileDetails.setFilename(file.getName());
                        fileDetails.setSize((int) file.length());
                        fileList.add(fileDetails);
                        count++;
                        if (count >= limit) {
                            break;
                        }
                    }
                }
            }
            logger.info("File list retrieved successfully");
            return fileList;
        } catch (Exception e) {
            logger.error("Error getting file list: " + e.getMessage());
            throw new FileException("Error getting file list");
        }
    }

    public void saveFileToStorage(byte[] fileBytes, String fileName) throws InputDataException {
        try {
            logger.info("Saving file to storage: " + fileName);
            String filePath = directory_path + File.separator + fileName;
            FileOutputStream outputStream = new FileOutputStream(filePath);
            outputStream.write(fileBytes);
            outputStream.close();
            logger.info("File saved successfully");
        } catch (IOException e) {
            logger.error("Error saving file: " + e.getMessage());
            throw new InputDataException("Error input data");
        }
    }

    public boolean deleteFileFromStorage(String filename) throws FileException {
        try {
            logger.info("Deleting file from storage: " + filename);
            File fileToDelete = new File(directory_path, filename);
            if (fileToDelete.exists() && fileToDelete.isFile()) {
                boolean deleted = fileToDelete.delete();
                if (deleted) {
                    logger.info("File deleted successfully");
                } else {
                    logger.warn("Failed to delete file: " + filename);
                }
                return deleted;
            }
        } catch (Exception e) {
            logger.error("Error deleting file: " + e.getMessage());
            throw new FileException("Error deleting file");
        }
        return false;
    }


    public File downloadFileFromRepository(String fileName) {
        logger.info("Downloading file from repository: " + fileName);
        return new File(directory_path, fileName);
    }

    public void renameFileOnRepository(String filename, String newFileName) throws FileException {
        try {
            logger.info("Renaming file on repository: " + filename + " to " + newFileName);
            int startIndex = newFileName.indexOf(":") + 2;
            String trimmedNewFileName = newFileName.substring(startIndex, newFileName.length() - 2);

            File file = new File(directory_path, filename);
            if (file.exists() && file.isFile()) {
                File newFile = new File(directory_path, trimmedNewFileName);
                boolean renamed = file.renameTo(newFile);
                if (renamed) {
                    logger.info("File renamed successfully: {} to {}", filename, newFileName);
                } else {
                    logger.warn("Failed to rename file: " + filename);
                }
            }
        } catch (Exception e) {
            logger.error("Error renaming file: " + e.getMessage());
            throw new FileException("Error renaming file");
        }
    }
}
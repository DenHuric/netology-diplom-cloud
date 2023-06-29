package com.dhuric.projects.netology_diploma_cloud_backend.repositories;

import com.dhuric.projects.netology_diploma_cloud_backend.dto.FileDetails;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.FileException;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.InputDataException;
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

    public List<FileDetails> getFileListFromStorage(int limit) throws FileException {
        try {
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
            return fileList;
        } catch (Exception e) {
            throw new FileException("Error getting file list");
        }
    }

    public void saveFileToStorage(byte[] fileBytes, String fileName) throws InputDataException {
        try {
            String filePath = directory_path + File.separator + fileName;
            FileOutputStream outputStream = new FileOutputStream(filePath);
            outputStream.write(fileBytes);
            outputStream.close();

        } catch (IOException e) {
            throw new InputDataException("Error input data");
        }
    }

    public boolean deleteFileFromStorage(String filename) throws FileException {
        try {
            File fileToDelete = new File(directory_path, filename);
            if (fileToDelete.exists() && fileToDelete.isFile()) {
                return fileToDelete.delete();
            }
        } catch (Exception e) {
            throw new FileException("Error delet file");
        }
        return false;
    }


    public File downloadFileFromRepository(String fileName) {
        return new File(directory_path, fileName);
    }

    public void renameFileOnRepository(String filename, String newFileName) throws FileException {
        try {
            int startIndex = newFileName.indexOf(":") + 2;
            String trimmedNewFileName = newFileName.substring(startIndex, newFileName.length() - 2);

            File file = new File(directory_path, filename);
            if (file.exists() && file.isFile()) {
                File newFile = new File(directory_path, trimmedNewFileName);
                file.renameTo(newFile);
            }
        } catch (Exception e) {
            throw new FileException("Error rename file");
        }

    }

}

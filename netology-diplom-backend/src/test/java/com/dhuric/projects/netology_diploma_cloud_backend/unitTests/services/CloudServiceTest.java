package com.dhuric.projects.netology_diploma_cloud_backend.unitTests.services;

import com.dhuric.projects.netology_diploma_cloud_backend.dto.FileDetails;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.FileException;
import com.dhuric.projects.netology_diploma_cloud_backend.exceprions.InputDataException;
import com.dhuric.projects.netology_diploma_cloud_backend.repositories.CloudRepository;
import com.dhuric.projects.netology_diploma_cloud_backend.services.AuthenticationService;
import com.dhuric.projects.netology_diploma_cloud_backend.services.CloudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CloudServiceTest {
    @Mock
    private CloudRepository cloudRepository;
    @Mock
    Logger logger;
    @Mock
    private AuthenticationService authenticationService;
    private CloudService cloudService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cloudService = new CloudService();
        cloudService.setCloudRepository(cloudRepository);
        cloudService.setAuthenticationService(authenticationService);
    }


    @Test
    void listFiles_ValidLimitAndAuthToken_NoExceptionThrown() throws InputDataException, FileException {
        int limit = 10;
        String authToken = "validAuthToken";
        doNothing().when(authenticationService).checkUserAuthToken(authToken);
        when(cloudRepository.getFileListFromStorage(limit)).thenReturn(List.of(new FileDetails("file1.txt", 1), new FileDetails("file2.txt", 2)));

        List<FileDetails> fileList = cloudService.listFiles(limit, authToken);

        assertNotNull(fileList);
        assertEquals(2, fileList.size());
        assertEquals("file1.txt", fileList.get(0).getFilename());
        assertEquals("file2.txt", fileList.get(1).getFilename());
        verify(authenticationService, times(1)).checkUserAuthToken(authToken);
        verify(cloudRepository, times(1)).getFileListFromStorage(limit);
    }

    @Test
    void listFiles_InvalidLimit_InputDataExceptionThrown() throws FileException {
        int limit = 0;
        String authToken = "validAuthToken";
        doNothing().when(authenticationService).checkUserAuthToken(authToken);

        assertThrows(InputDataException.class, () -> cloudService.listFiles(limit, authToken));
        verify(authenticationService, times(1)).checkUserAuthToken(authToken);
        verify(cloudRepository, never()).getFileListFromStorage(limit);
    }

    @Test
    void deleteFile_ValidFileNameAndAuthToken_NoExceptionThrown() throws FileException, InputDataException {
        String fileName = "file.txt";
        String authToken = "validAuthToken";
        doNothing().when(authenticationService).checkUserAuthToken(authToken);
        when(cloudRepository.deleteFileFromStorage(fileName)).thenReturn(true);

        boolean deleted = cloudService.deleteFile(fileName, authToken);

        assertTrue(deleted);
        verify(authenticationService, times(1)).checkUserAuthToken(authToken);
        verify(cloudRepository, times(1)).deleteFileFromStorage(fileName);
    }

    @Test
    void deleteFile_InvalidFileName_InputDataExceptionThrown() throws FileException {
        String fileName = null;
        String authToken = "validAuthToken";

        assertThrows(InputDataException.class, () -> {
            authenticationService.checkUserAuthToken(authToken);
            cloudService.deleteFile(fileName, authToken);
        });

        verify(authenticationService, times(1)).checkUserAuthToken(authToken);
        verify(cloudRepository, never()).deleteFileFromStorage(fileName);
    }


    @Test
    void saveFile_ValidFileDataAndAuthToken_NoExceptionThrown() throws InputDataException, FileException {
        byte[] fileData = "File data".getBytes();
        String fileName = "file.txt";
        String authToken = "validAuthToken";
        doNothing().when(authenticationService).checkUserAuthToken(authToken);

        cloudService.saveFile(fileData, fileName, authToken);

        verify(authenticationService, times(1)).checkUserAuthToken(authToken);
        verify(cloudRepository, times(1)).saveFileToStorage(fileData, fileName);
    }


    @Test
    void saveFile_InvalidFileName_InputDataExceptionThrown() throws InputDataException {
        byte[] fileData = "File data".getBytes();
        String fileName = null;
        String authToken = "validAuthToken";

        assertThrows(InputDataException.class, () -> {
            authenticationService.checkUserAuthToken(authToken);
            cloudService.saveFile(fileData, fileName, authToken);
        });

        verify(authenticationService, times(1)).checkUserAuthToken(authToken);
        verify(cloudRepository, never()).saveFileToStorage(fileData, fileName);
    }


    @Test
    void downloadFile_ValidFileNameAndAuthToken_NoExceptionThrown() throws InputDataException, FileException, IOException, IOException {
        String fileName = "file.txt";
        String authToken = "validAuthToken";
        doNothing().when(authenticationService).checkUserAuthToken(authToken);

        File tempFile = File.createTempFile("temp", ".txt");
        FileWriter writer = new FileWriter(tempFile);
        writer.write("file.txt");
        writer.close();

        when(cloudRepository.downloadFileFromRepository(fileName)).thenReturn(tempFile);

        byte[] fileBytes = cloudService.downloadFile(fileName, authToken);

        assertNotNull(fileBytes);
        assertEquals("file.txt", new String(fileBytes));
        verify(authenticationService, times(1)).checkUserAuthToken(authToken);
        verify(cloudRepository, times(1)).downloadFileFromRepository(fileName);

        tempFile.delete();
    }


    @Test
    void downloadFile_InvalidFileName_InputDataExceptionThrown() {
        String fileName = null;
        String authToken = "validAuthToken";
        doNothing().when(authenticationService).checkUserAuthToken(authToken);

        assertThrows(InputDataException.class, () -> cloudService.downloadFile(fileName, authToken));
        verify(authenticationService, times(1)).checkUserAuthToken(authToken);
        verify(cloudRepository, never()).downloadFileFromRepository(fileName);
    }

    @Test
    void renameFile_ValidFileNamesAndAuthToken_NoExceptionThrown() throws InputDataException, FileException {
        String fileName = "file.txt";
        String newFileName = "newFile.txt";
        String authToken = "validAuthToken";
        doNothing().when(authenticationService).checkUserAuthToken(authToken);

        cloudService.renameFile(fileName, newFileName, authToken);

        verify(authenticationService, times(1)).checkUserAuthToken(authToken);
        verify(cloudRepository, times(1)).renameFileOnRepository(fileName, newFileName);
    }

    @Test
    void renameFile_InvalidFileNames_InputDataExceptionThrown() throws FileException {
        String fileName = null;
        String newFileName = "newFile.txt";
        String authToken = "validAuthToken";
        doNothing().when(authenticationService).checkUserAuthToken(authToken);

        assertThrows(InputDataException.class, () -> cloudService.renameFile(fileName, newFileName, authToken));
        verify(authenticationService, times(1)).checkUserAuthToken(authToken);
        verify(cloudRepository, never()).renameFileOnRepository(fileName, newFileName);
    }
}

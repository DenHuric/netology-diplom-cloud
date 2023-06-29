package com.dhuric.projects.netology_diploma_cloud_backend;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
public class NetologyDiplomaCloudBackendApplication implements ApplicationRunner {

    @Autowired
    private Environment environment;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(NetologyDiplomaCloudBackendApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String storeDirectoryPath = environment.getProperty("cloud.storeDirectory");
        File storeDirectory = new File(storeDirectoryPath);

        if (!storeDirectory.exists()) {
            boolean created = storeDirectory.mkdirs();
            if (created) {
                System.out.println("Folder successfully created: " + storeDirectory.getAbsolutePath());
            } else {
                System.out.println("Failed to create folder: " + storeDirectory.getAbsolutePath());
            }
        } else {
            System.out.println("Folder already exists: " + storeDirectory.getAbsolutePath());
        }
    }
}

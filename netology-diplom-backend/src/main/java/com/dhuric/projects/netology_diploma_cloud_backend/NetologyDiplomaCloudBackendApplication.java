package com.dhuric.projects.netology_diploma_cloud_backend;


import com.dhuric.projects.netology_diploma_cloud_backend.repositories.CloudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(CloudRepository.class);

    public static void main(String[] args) throws IOException {
        SpringApplication.run(NetologyDiplomaCloudBackendApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String storeDirectoryPath = environment.getProperty("cloud.storeDirectory");
        String logDirectoryPath = environment.getProperty("cloud.logDirectory");
        File storeDirectory = new File(storeDirectoryPath);
        File logDirectory = new File(logDirectoryPath);

        if (!storeDirectory.exists()) {
            boolean created = storeDirectory.mkdirs();
            if (created) {
                logger.info("Store folder successfully created: " + storeDirectory.getAbsolutePath());
            } else {
                logger.info("Failed to create store folder: " + storeDirectory.getAbsolutePath());
            }
        }
        if (!logDirectory.exists()) {
            boolean created = logDirectory.mkdirs();
            if (created) {
                System.out.println("Logs folder successfully created: " + storeDirectory.getAbsolutePath());
            } else {
                System.out.println("Failed to create logs folder: " + storeDirectory.getAbsolutePath());
            }
        }
    }
}

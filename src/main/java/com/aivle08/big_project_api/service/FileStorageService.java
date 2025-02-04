package com.aivle08.big_project_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored: "
                    + this.fileStorageLocation, ex);
        }
    }

    public List<String> storeFiles(List<MultipartFile> files, String id) {
        List<String> storedFiles = new ArrayList<>();
        Path idDirectory = fileStorageLocation.resolve(id);

        try {
            Files.createDirectories(idDirectory);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create directory for ID: " + id, ex);
        }

        for (MultipartFile file : files) {
            String fileName = storeFile(file, idDirectory);
            storedFiles.add(fileName);
        }

        return storedFiles;
    }

    private String storeFile(MultipartFile file, Path idDirectory) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("File name cannot be null.");
        }

        String fileName = StringUtils.cleanPath(originalFilename);

        if (fileName.contains("..")) {
            throw new IllegalArgumentException("Invalid file path: " + fileName);
        }

        if (!fileName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed: " + fileName);
        }

        try {
            Path targetLocation = idDirectory.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file: " + fileName, ex);
        }
    }

    public File getFile(String recruitmentId, String fileName) {
        Path recruitmentDirectory = this.fileStorageLocation.resolve(recruitmentId);

        File folder = recruitmentDirectory.toFile();
        if (!folder.exists()) {
            throw new RuntimeException("Recruitment folder does not exist");
        }

        File file = new File(recruitmentDirectory.toFile(), fileName);
        if (!file.exists()) {
            throw new RuntimeException("File not found for Recruitment ID: " + recruitmentId + " and file name: " + fileName);
        }

        return file;
    }
}

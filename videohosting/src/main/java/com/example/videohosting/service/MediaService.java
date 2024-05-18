package com.example.videohosting.service;

import com.example.videohosting.exception.DeleteFileException;
import com.example.videohosting.exception.LoadFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class MediaService {
    @Value("${videohosting.app.mediaRoot}")
    private String mediaRoot;

    public void saveMedia(MultipartFile file, String filePath) {
        try {
            Path path = Paths.get(mediaRoot + filePath);
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new LoadFileException("File upload error");
        }
    }

    public Resource getMedia(String filePath) {
        try {
            Path path = Paths.get(mediaRoot + filePath).normalize();
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new LoadFileException("File upload error");
            }
        } catch (MalformedURLException e) {
            throw new LoadFileException("File upload error");
        }
    }

    public void deleteMedia(String filePath) {
        try {
            Path path = Paths.get(mediaRoot + filePath);
            Files.delete(path);
        } catch (IOException ex) {
            throw new DeleteFileException("The file could not be deleted");
        }
    }
}

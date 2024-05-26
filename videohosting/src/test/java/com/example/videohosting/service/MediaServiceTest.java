package com.example.videohosting.service;

import org.jcodec.api.JCodecException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"videohosting.app.mediaRoot=C:/server/testMedia/"})
@ExtendWith(MockitoExtension.class)
class MediaServiceTest {

    @Value("${videohosting.app.mediaRoot}")
    private String mediaRoot;

    @Autowired
    private MediaService mediaService;

    @Test
    void saveMedia() throws IOException {
        String filePath = "video/2.mp4";
        MockMultipartFile file = new MockMultipartFile("file", "test.mp4", "video/mp4", "video content".getBytes());
        mediaService.saveMedia(file, filePath);
        Path path = Paths.get(mediaRoot + filePath);
        assertTrue(Files.exists(path));
        Files.delete(path);
    }


    @Test
    void getMedia() throws IOException {
        String filePath = "video/2.mp4";
        Path path = Paths.get(mediaRoot + filePath);
        Files.createFile(path);
        Resource resource = mediaService.getMedia(filePath);
        assertNotNull(resource);
        Files.delete(path);
    }

    @Test
    void deleteMedia() throws IOException {
        String filePath = "video/2.mp4";
        Path path = Paths.get(mediaRoot + filePath);
        Files.createFile(path);
        mediaService.deleteMedia(filePath);
        assertFalse(Files.exists(path));
    }

    @Test
    void getDuration() throws IOException, JCodecException {
        String videoPath = "video/1.mp4";
        Long duration = mediaService.getDuration(videoPath);
        assertEquals(20L, duration);
    }
}
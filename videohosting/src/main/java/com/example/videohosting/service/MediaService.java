package com.example.videohosting.service;

import com.example.videohosting.exception.DeleteFileException;
import com.example.videohosting.exception.LoadFileException;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class MediaService {
    @Value("${videohosting.app.mediaRoot}")
    private String mediaRoot;
    private final Logger logger = LoggerFactory.getLogger(MediaService.class);

    public void saveMedia(MultipartFile file, String filePath) {
        try {
            logger.info("Attempting to save media at path: {}", filePath);
            Path path = Paths.get(mediaRoot + filePath);
            Files.write(path, file.getBytes());
            logger.info("Media saved successfully at path: {}", filePath);
        } catch (IOException e) {
            logger.error("Error saving media at path: {}", filePath, e);
            throw new LoadFileException("File upload error");
        }
    }

    public Resource getMedia(String filePath) {
        try {
            logger.info("Attempting to retrieve media at path: {}", filePath);
            Path path = Paths.get(mediaRoot + filePath).normalize();
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() && resource.isReadable()) {
                logger.info("Media retrieved successfully at path: {}", filePath);
                return resource;
            } else {
                logger.warn("Media at path {} is not readable or does not exist", filePath);
                throw new LoadFileException("File upload error");
            }
        } catch (MalformedURLException e) {
            logger.error("Malformed URL for media at path: {}", filePath, e);
            throw new LoadFileException("File upload error");
        }
    }

    public void deleteMedia(String filePath) {
        try {
            logger.info("Attempting to delete media at path: {}", filePath);
            Path path = Paths.get(mediaRoot + filePath);
            Files.delete(path);
            logger.info("Media deleted successfully at path: {}", filePath);
        } catch (IOException ex) {
            logger.error("Error deleting media at path: {}", filePath, ex);
            throw new DeleteFileException("The file could not be deleted");
        }
    }

    public Long getDuration(String videoPath) throws IOException, JCodecException {
        logger.info("Attempting to get duration of video at path: {}", videoPath);
        File file = new File(mediaRoot + videoPath );
        FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(file));
        Double duration = grab.getVideoTrack().getMeta().getTotalDuration();
        logger.info("Duration of video at path {} is {} seconds", videoPath, duration);
        return duration.longValue();
    }
}

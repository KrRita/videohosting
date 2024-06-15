package com.example.videohosting.controller;

import com.example.videohosting.exception.LoadFileException;
import com.example.videohosting.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping("/videos/{id}")
    public ResponseEntity<Resource> getVideo(@PathVariable Long id) {
        String videoPath = "video\\" + id + ".mp4";
        Resource video = mediaService.getMedia(videoPath);
        return ResponseEntity.status(HttpStatus.OK).body(video);
    }

    @GetMapping("/videosPreview/{id}")
    public ResponseEntity<Resource> getVideoPreview(@PathVariable Long id) {
        String videoPreview = "previewVideo\\" + id + ".jpeg";
        Resource preview;
        try {
            preview = mediaService.getMedia(videoPreview);
            return ResponseEntity.status(HttpStatus.OK).body(preview);
        } catch (LoadFileException ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @GetMapping("/playlistsIcon/{id}")
    public ResponseEntity<Resource> getPlaylistIcon(@PathVariable Long id) {
        String imagePath = "imageIconPlaylist\\" + id + ".jpeg";
        Resource image;
        try {
            image = mediaService.getMedia(imagePath);
            return ResponseEntity.status(HttpStatus.OK).body(image);
        } catch (LoadFileException ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @GetMapping("/usersHeader/{id}")
    public ResponseEntity<Resource> getUserHeader(@PathVariable Long id) {
        String headerPath = "imageHeaderUser\\" + id + ".jpeg";
        Resource header;
        try {
            header = mediaService.getMedia(headerPath);
            return ResponseEntity.status(HttpStatus.OK).body(header);
        } catch (LoadFileException ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @GetMapping("/usersIcon/{id}")
    public ResponseEntity<Resource> getUserIcon(@PathVariable Long id) {
        String iconPath = "imageIconUser\\" + id + ".jpeg";
        Resource icon;
        try {
            icon = mediaService.getMedia(iconPath);
            return ResponseEntity.status(HttpStatus.OK).body(icon);
        } catch (LoadFileException ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

}

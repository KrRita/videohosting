package com.example.videohosting.repository;

import com.example.videohosting.entity.ViewedVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewedVideoRepository extends JpaRepository<ViewedVideo, Long> {
    List<ViewedVideo> getViewedVideosByIdUser(Long idUser);
    Long countViewedVideosByVideo_IdVideo(Long idVideo);
}

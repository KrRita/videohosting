package com.example.videohosting.repository;

import com.example.videohosting.entity.AssessmentVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface AssessmentVideoRepository extends JpaRepository<AssessmentVideo, Long> {
    List<AssessmentVideo> getAssessmentVideoByIdUserAndLiked(Long idUser, Boolean liked);
    Long countAssessmentVideosByVideo_IdVideoAndLiked(Long idVideo, Boolean liked);
    @Query("SELECT av.idAssessmentVideo FROM AssessmentVideo av WHERE av.idUser=:idUser AND av.video.idVideo=:idVideo" )
    Long getAssessmentVideoByIdUserAndVideo_IdVideo(Long idUser, Long idVideo);
}

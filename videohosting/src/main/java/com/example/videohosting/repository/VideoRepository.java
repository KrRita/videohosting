package com.example.videohosting.repository;

import com.example.videohosting.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query(value = "SELECT v.id_video,v.id_user,v.video_name,v.duration,v.description,v.release_date_time " +
                   "FROM User_ u1 JOIN Subscription s ON u1.id_user = s.id_user_subscriber " +
                   "JOIN Video v ON s.id_user_channel = v.id_user WHERE u1.id_user = :idUser",
            nativeQuery = true)
    List<Video> getVideosBySubscription(Long idUser);
    List<Video> findByNameContaining(String videoName);
    List<Video> findByUser_ChannelNameContaining(String name);
    List<Video> findDistinctByCategories_NameIn(List<String> categories);
}

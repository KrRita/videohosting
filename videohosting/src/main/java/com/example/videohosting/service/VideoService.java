package com.example.videohosting.service;

import com.example.videohosting.entity.AssessmentVideo;
import com.example.videohosting.entity.Category;
import com.example.videohosting.entity.PlaylistWithVideos;
import com.example.videohosting.entity.Video;
import com.example.videohosting.entity.ViewedVideo;
import com.example.videohosting.exception.DeleteFileException;
import com.example.videohosting.exception.LoadFileException;
import com.example.videohosting.exception.NotFoundException;
import com.example.videohosting.mapper.AssessmentVideoMapper;
import com.example.videohosting.mapper.PlaylistWithVideosMapper;
import com.example.videohosting.mapper.VideoMapper;
import com.example.videohosting.mapper.ViewedVideoMapper;
import com.example.videohosting.model.AssessmentVideoModel;
import com.example.videohosting.model.PlaylistWithVideosModel;
import com.example.videohosting.model.VideoModel;
import com.example.videohosting.model.ViewedVideoModel;
import com.example.videohosting.repository.AssessmentVideoRepository;
import com.example.videohosting.repository.PlaylistWithVideosRepository;
import com.example.videohosting.repository.UserRepository;
import com.example.videohosting.repository.VideoRepository;
import com.example.videohosting.repository.ViewedVideoRepository;
import org.jcodec.api.JCodecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final VideoMapper videoMapper;
    private final ViewedVideoRepository viewedVideoRepository;
    private final ViewedVideoMapper viewedVideoMapper;
    private final AssessmentVideoRepository assessmentVideoRepository;
    private final AssessmentVideoMapper assessmentVideoMapper;
    private final PlaylistWithVideosRepository playlistWithVideosRepository;
    private final PlaylistWithVideosMapper playlistWithVideosMapper;
    private final VideoServiceUtils videoServiceUtils;
    private final MediaService mediaService;
    private final Logger logger = LoggerFactory.getLogger(VideoService.class);

    @Autowired
    public VideoService(VideoRepository videoRepository, UserRepository userRepository, VideoMapper videoMapper,
                        ViewedVideoRepository viewedVideoRepository, ViewedVideoMapper viewedVideoMapper,
                        AssessmentVideoRepository assessmentVideoRepository,
                        AssessmentVideoMapper assessmentVideoMapper,
                        PlaylistWithVideosRepository playlistWithVideosRepository,
                        PlaylistWithVideosMapper playlistWithVideosMapper,
                        VideoServiceUtils videoServiceUtils, MediaService mediaService) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
        this.videoMapper = videoMapper;
        this.viewedVideoRepository = viewedVideoRepository;
        this.viewedVideoMapper = viewedVideoMapper;
        this.assessmentVideoRepository = assessmentVideoRepository;
        this.assessmentVideoMapper = assessmentVideoMapper;
        this.playlistWithVideosRepository = playlistWithVideosRepository;
        this.playlistWithVideosMapper = playlistWithVideosMapper;
        this.videoServiceUtils = videoServiceUtils;
        this.mediaService = mediaService;
    }


    @CachePut(value = "videos", key = "#result.idVideo")
    public VideoModel insertVideo(VideoModel videoModel, MultipartFile videoFile,
                                  MultipartFile preview) throws IOException, JCodecException {
        logger.info("Inserting video: {}", videoModel.getName());
        if (videoFile == null) {
            logger.error("File upload error: video file is null");
            throw new LoadFileException("File upload error");
        }
        videoModel.setReleaseDateTime(Timestamp.valueOf(LocalDateTime.now()));
        Video video = videoMapper.toEntity(videoModel);
        video.setCategories(videoServiceUtils.toCategoryEntityList(videoModel.getCategories()));
        Video savedVideo = videoRepository.save(video);
        if (preview != null) {
            String path = "previewVideo\\" + savedVideo.getIdVideo() + ".jpeg";
            mediaService.saveMedia(preview, path);
            logger.info("Saved image preview for video at path: {}", path);
        }
        String videoPath = "video\\" + savedVideo.getIdVideo() + ".mp4";
        mediaService.saveMedia(videoFile, videoPath);
        logger.info("Saved video at path: {}", videoPath);

        Long duration = mediaService.getDuration(videoPath);
        savedVideo.setDuration(duration);
        Video savedVideoWithDuration = videoRepository.save(savedVideo);

        VideoModel savedVideoModel = videoMapper.toModel(savedVideoWithDuration);
        savedVideoModel.getUser().setCountSubscribers(
                userRepository.getSubscribersCountByIdUser(savedVideo.getUser().getIdUser()));
        List<String> categories = videoServiceUtils.toCategoryStringList(savedVideo.getCategories());
        savedVideoModel.setCategories(categories);
        Long count = 0L;
        savedVideoModel.setCountViewing(count);
        savedVideoModel.setCountDislikes(count);
        savedVideoModel.setCountLikes(count);
        logger.info("Video inserted: {}", savedVideoModel.getIdVideo());
        return savedVideoModel;
    }

    @CachePut(value = "videos", key = "#result.idVideo")
    public VideoModel updateVideo(VideoModel videoModel, MultipartFile previewImage) {
        logger.info("Updating video: {}", videoModel.getIdVideo());
        Video newVideo = videoMapper.toEntity(videoModel);
        Video oldVideo = videoRepository.findById(videoModel.getIdVideo())
                .orElseThrow(() -> {
                    logger.error("Video with id {} not found", videoModel.getIdVideo());
                    return new NotFoundException("Video not found");
                });
        if (newVideo.getName() != null) {
            oldVideo.setName(newVideo.getName());
            logger.info("Updated video name to: {}", newVideo.getName());
        }
        if (newVideo.getDescription() != null) {
            oldVideo.setDescription(newVideo.getDescription());
            logger.info("Updated description to: {}", newVideo.getDescription());
        }
        if (previewImage != null) {
            String path = "previewVideo\\" + oldVideo.getIdVideo() + ".jpeg";
            mediaService.saveMedia(previewImage, path);
            logger.info("Update image preview for video at path: {}", path);
        }
        Video savedVideo = videoRepository.save(oldVideo);
        VideoModel savedVideoModel = videoMapper.toModel(savedVideo);
        savedVideoModel.getUser().setCountSubscribers(
                userRepository.getSubscribersCountByIdUser(savedVideo.getUser().getIdUser()));
        savedVideoModel.setCategories(videoServiceUtils.toCategoryStringList(savedVideo.getCategories()));
        Long idVideo = savedVideoModel.getIdVideo();
        Long countViews = viewedVideoRepository.countViewedVideosByVideo_IdVideo(idVideo);
        savedVideoModel.setCountViewing(countViews);
        Long countLikes = assessmentVideoRepository
                .countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, true);
        savedVideoModel.setCountLikes(countLikes);
        Long countDislikes = assessmentVideoRepository
                .countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, false);
        savedVideoModel.setCountDislikes(countDislikes);
        logger.info("Video updated: {}", savedVideoModel.getIdVideo());
        return savedVideoModel;
    }

    @CacheEvict(value = "videos", key = "#id")
    public void deleteVideo(Long id) {
        logger.info("Deleting video: {}", id);
        String videoPath = "video\\" + id + ".mp4";
        String previewPath = "previewVideo\\" + id + ".jpeg";
        videoRepository.deleteById(id);
        logger.info("Video deleted with id: {}", id);
        try {
            mediaService.deleteMedia(videoPath);
            logger.info("Video deleted");
        } catch (DeleteFileException ex) {
            logger.warn("Failed to delete associated media");
        }
        try {
            mediaService.deleteMedia(previewPath);
            logger.info("Associated media deleted");
        } catch (DeleteFileException ex) {
            logger.warn("Failed to delete associated media");
        }
    }

    @Cacheable(value = "videos", key = "#id")
    public VideoModel findVideoById(Long id) {
        logger.info("Finding video by id: {}", id);
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Video with id {} not found", id);
                    return new NotFoundException("Video not found");
                });
        Long countViews = viewedVideoRepository.countViewedVideosByVideo_IdVideo(id);
        Long countLikes = assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(id, true);
        Long countDislikes = assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(id, false);
        VideoModel videoModel = videoMapper.toModel(video);
        videoModel.getUser().setCountSubscribers(
                userRepository.getSubscribersCountByIdUser(videoModel.getUser().getIdUser()));
        videoModel.setCountViewing(countViews);
        videoModel.setCountLikes(countLikes);
        videoModel.setCountDislikes(countDislikes);
        videoModel.setCategories(videoServiceUtils.toCategoryStringList(video.getCategories()));
        logger.info("Video found: {}", id);
        return videoModel;
    }

    @Transactional
    public List<VideoModel> getSubscriptionsVideos(Long idUser) {
        logger.info("Getting subscription videos for user: {}", idUser);
        List<Video> videos = videoRepository.getVideosBySubscription(idUser);
        List<VideoModel> videoModels = videoServiceUtils.getVideoModelListWithCategories(videos);
        logger.info("Subscriptions videos retrieved for user: {}", idUser);
        return videoServiceUtils.addFieldInModelList(videoModels);
    }

    @Transactional
    public List<ViewedVideoModel> getViewedVideos(Long idUser) {
        logger.info("Getting viewed videos for user: {}", idUser);
        List<ViewedVideo> viewedVideos = viewedVideoRepository.getViewedVideosByIdUser(idUser);
        List<ViewedVideoModel> viewedVideoModels = viewedVideoMapper.toModelList(viewedVideos);
        for (ViewedVideoModel viewedVideoModel : viewedVideoModels) {
            Long idVideo = viewedVideoModel.getVideo().getIdVideo();
            viewedVideoModel.getVideo().getUser().setCountSubscribers(
                    userRepository.getSubscribersCountByIdUser(viewedVideoModel.getVideo().getUser().getIdUser()));
            Long countViews = viewedVideoRepository.countViewedVideosByVideo_IdVideo(idVideo);
            Long countLikes = assessmentVideoRepository
                    .countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, true);
            Long countDislikes = assessmentVideoRepository
                    .countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, false);
            viewedVideoModel.getVideo().setCountViewing(countViews);
            viewedVideoModel.getVideo().setCountLikes(countLikes);
            viewedVideoModel.getVideo().setCountDislikes(countDislikes);
        }
        Iterator<ViewedVideo> viewedVideoIterator = viewedVideos.iterator();
        Iterator<ViewedVideoModel> viewedVideoModelIterator = viewedVideoModels.iterator();
        while (viewedVideoIterator.hasNext() && viewedVideoModelIterator.hasNext()) {
            List<Category> categories = viewedVideoIterator.next().getVideo().getCategories();
            viewedVideoModelIterator.next().getVideo().setCategories(
                    videoServiceUtils.toCategoryStringList(categories));
        }
        logger.info("Viewed videos retrieved for user: {}", idUser);
        return viewedVideoModels;
    }

    @Transactional
    public List<VideoModel> getVideosByName(String name) {
        logger.info("Getting videos by name: {}", name);
        List<Video> videos = videoRepository.findByNameContaining(name);
        List<VideoModel> videoModels = videoServiceUtils.getVideoModelListWithCategories(videos);
        logger.info("Videos retrieved by name: {}", name);
        return videoServiceUtils.addFieldInModelList(videoModels);
    }

    @Transactional
    public List<VideoModel> getVideosByUserName(String name) {
        logger.info("Getting videos by user name: {}", name);
        List<Video> videos = videoRepository.findByUser_ChannelNameContaining(name);
        List<VideoModel> videoModels = videoServiceUtils.getVideoModelListWithCategories(videos);
        logger.info("Videos retrieved by user name: {}", name);
        return videoServiceUtils.addFieldInModelList(videoModels);
    }

    @Transactional
    public List<VideoModel> getVideosByCategories(List<String> categories) {
        logger.info("Getting videos by categories: {}", categories);
        List<Video> videos = videoRepository.findDistinctByCategories_NameIn(categories);
        List<VideoModel> videoModels = videoServiceUtils.getVideoModelListWithCategories(videos);
        logger.info("Videos retrieved by categories: {}", categories);
        return videoServiceUtils.addFieldInModelList(videoModels);
    }

    @CacheEvict(value = "videos", key = "#assessmentVideoModel.video.idVideo")
    public VideoModel insertAssessmentVideo(AssessmentVideoModel assessmentVideoModel) {
        logger.info("Inserting assessment video: {}", assessmentVideoModel);
        AssessmentVideo assessmentVideo = assessmentVideoMapper.toEntity(assessmentVideoModel);
        assessmentVideo.setDateOfAssessment(Timestamp.valueOf(LocalDateTime.now()));
        assessmentVideoRepository.save(assessmentVideo);
        logger.info("Assessment video inserted successfully");
        return findVideoById(assessmentVideoModel.getVideo().getIdVideo());
    }

    @CacheEvict(value = "videos", key = "#idVideo")
    public VideoModel deleteAssessmentVideo(Long idUser, Long idVideo) {
        logger.info("Deleting assessment video for user ID: {} and video ID: {}", idUser, idVideo);
        Long id = assessmentVideoRepository.getAssessmentVideoByIdUserAndVideo_IdVideo(idUser, idVideo);
        assessmentVideoRepository.deleteById(id);
        logger.info("Deleted assessment video with ID: {} successfully", id);
        return findVideoById(idVideo);
    }

    public VideoModel insertViewedVideo(ViewedVideoModel viewedVideoModel) {
        logger.info("Inserting viewed video: {}", viewedVideoModel);
        ViewedVideo viewedVideo = viewedVideoMapper.toEntity(viewedVideoModel);
        viewedVideo.setDateOfViewing(Timestamp.valueOf(LocalDateTime.now()));
        viewedVideoRepository.save(viewedVideo);
        logger.info("Viewed video inserted successfully");
        return findVideoById(viewedVideoModel.getVideo().getIdVideo());
    }

    @Transactional
    public List<VideoModel> getLikedVideos(Long idUser) {
        logger.info("Getting liked videos of user with id: {}", idUser);
        List<AssessmentVideo> assessmentVideos =
                assessmentVideoRepository.getAssessmentVideoByIdUserAndLiked(idUser, true);
        List<Video> videos = new ArrayList<>();
        assessmentVideos
                .forEach(assessmentVideo -> videos.add(assessmentVideo.getVideo()));
        List<VideoModel> videoModels = videoServiceUtils.getVideoModelListWithCategories(videos);
        logger.info("Videos retrieved by according to the user's assessment with id: {}", idUser);
        return videoServiceUtils.addFieldInModelList(videoModels);
    }

    @Transactional
    public List<PlaylistWithVideosModel> getVideosFromPlaylist(Long idPlaylist) {
        logger.info("Getting videos from playlist with id: {}", idPlaylist);
        List<PlaylistWithVideos> playlistWithVideos =
                playlistWithVideosRepository.getPlaylistWithVideosByIdPlaylist(idPlaylist);
        List<PlaylistWithVideosModel> playlistWithVideosModels =
                playlistWithVideosMapper.toModelList(playlistWithVideos);
        Iterator<PlaylistWithVideos> playlistWithVideosIterator = playlistWithVideos.iterator();
        Iterator<PlaylistWithVideosModel> playlistWithVideosModelIterator = playlistWithVideosModels.iterator();
        while (playlistWithVideosModelIterator.hasNext() && playlistWithVideosIterator.hasNext()) {
            VideoModel videoModel = playlistWithVideosModelIterator.next().getVideo();
            videoModel.getUser().setCountSubscribers(
                    userRepository.getSubscribersCountByIdUser(videoModel.getUser().getIdUser()));
            Long idVideoModel = videoModel.getIdVideo();
            List<Category> categories = playlistWithVideosIterator.next().getVideo().getCategories();
            List<String> categoryStrings = videoServiceUtils.toCategoryStringList(categories);
            videoModel.setCategories(categoryStrings);
            Long countViews = viewedVideoRepository.countViewedVideosByVideo_IdVideo(idVideoModel);
            Long countLikes = assessmentVideoRepository
                    .countAssessmentVideosByVideo_IdVideoAndLiked(idVideoModel, true);
            Long countDislikes = assessmentVideoRepository
                    .countAssessmentVideosByVideo_IdVideoAndLiked(idVideoModel, false);
            videoModel.setCountViewing(countViews);
            videoModel.setCountLikes(countLikes);
            videoModel.setCountDislikes(countDislikes);
        }
        logger.info("Videos retrieved from playlist with id: {}", idPlaylist);
        return playlistWithVideosModels;
    }

    @Transactional
    public List<PlaylistWithVideosModel> insertPlaylistWithVideos(PlaylistWithVideosModel playlistWithVideosModel) {
        logger.info("Inserting video in playlist: {}", playlistWithVideosModel);
        PlaylistWithVideos playlistWithVideos = playlistWithVideosMapper.toEntity(playlistWithVideosModel);
        playlistWithVideos.setDateOfAddition(Timestamp.valueOf(LocalDateTime.now()));
        playlistWithVideosRepository.save(playlistWithVideos);
        logger.info("Inserted video in playlist successfully");
        return getVideosFromPlaylist(playlistWithVideosModel.getIdPlaylist());
    }

    @Transactional
    public List<PlaylistWithVideosModel> deletePlaylistWithVideos(Long idPlaylist, Long idVideo) {
        logger.info("Deleting video from playlist for playlist ID: {} and video ID: {}", idPlaylist, idVideo);
        Long id = playlistWithVideosRepository
                .getPlaylistWithVideosByIdPlaylistAndVideo_IdVideo(idPlaylist, idVideo);
        playlistWithVideosRepository.deleteById(id);
        logger.info("Deleted video from playlist with entity ID : {} successfully", id);
        return getVideosFromPlaylist(idPlaylist);
    }
}


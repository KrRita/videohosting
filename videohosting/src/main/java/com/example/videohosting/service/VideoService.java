package com.example.videohosting.service;

import com.example.videohosting.entity.AssessmentVideo;
import com.example.videohosting.entity.Category;
import com.example.videohosting.entity.PlaylistWithVideos;
import com.example.videohosting.entity.Video;
import com.example.videohosting.entity.ViewedVideo;
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
import com.example.videohosting.repository.VideoRepository;
import com.example.videohosting.repository.ViewedVideoRepository;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    private final ViewedVideoRepository viewedVideoRepository;
    private final ViewedVideoMapper viewedVideoMapper;
    private final AssessmentVideoRepository assessmentVideoRepository;
    private final AssessmentVideoMapper assessmentVideoMapper;
    private final PlaylistWithVideosRepository playlistWithVideosRepository;
    private final PlaylistWithVideosMapper playlistWithVideosMapper;
    private final VideoServiceUtils videoServiceUtils;
    private final MediaService mediaService;

    @Value("${videohosting.app.mediaRoot}")
    private String mediaRoot;

    @Autowired
    public VideoService(VideoRepository videoRepository, VideoMapper videoMapper,
                        ViewedVideoRepository viewedVideoRepository, ViewedVideoMapper viewedVideoMapper,
                        AssessmentVideoRepository assessmentVideoRepository,
                        AssessmentVideoMapper assessmentVideoMapper,
                        PlaylistWithVideosRepository playlistWithVideosRepository,
                        PlaylistWithVideosMapper playlistWithVideosMapper,
                        VideoServiceUtils videoServiceUtils, MediaService mediaService) {
        this.videoRepository = videoRepository;
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


    public VideoModel insertVideo(VideoModel videoModel, MultipartFile videoFile, MultipartFile preview) throws IOException, JCodecException {
        if (videoFile == null) {
            throw new LoadFileException("File upload error");
        }
        videoModel.setReleaseDateTime(Timestamp.valueOf(LocalDateTime.now()));
        Video video = videoMapper.toEntity(videoModel);
        video.setCategories(videoServiceUtils.toCategoryEntityList(videoModel.getCategories()));
        Video savedVideo = videoRepository.save(video);
        if (preview != null) {
            String path = "previewVideo\\" + savedVideo.getIdVideo() + ".jpeg";
            mediaService.saveMedia(preview, path);
        }
        String videoPath = "video\\" + savedVideo.getIdVideo() + ".mp4";
        mediaService.saveMedia(videoFile, videoPath);

        File file = new File(mediaRoot + videoPath );
        FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(file));
        Double duration = grab.getVideoTrack().getMeta().getTotalDuration();
        savedVideo.setDuration(duration.longValue());
        Video savedVideoWithDuration = videoRepository.save(savedVideo);

        VideoModel savedVideoModel = videoMapper.toModel(savedVideoWithDuration);
        List<String> categories = videoServiceUtils.toCategoryStringList(savedVideo.getCategories());
        savedVideoModel.setCategories(categories);
        Long count = 0L;
        savedVideoModel.setCountViewing(count);
        savedVideoModel.setCountDislikes(count);
        savedVideoModel.setCountLikes(count);
        return savedVideoModel;
    }

    public VideoModel updateVideo(VideoModel videoModel, MultipartFile previewImage) {
        Video newVideo = videoMapper.toEntity(videoModel);
        Video oldVideo = videoRepository.findById(videoModel.getIdVideo()).orElseThrow(() -> new NotFoundException("Video not found"));
        if (newVideo.getName() != null) {
            oldVideo.setName(newVideo.getName());
        }
        if (newVideo.getDescription() != null) {
            oldVideo.setDescription(newVideo.getDescription());
        }
        if (previewImage != null) {
            String path = "previewVideo\\" + oldVideo.getIdVideo() + ".jpeg";
            mediaService.saveMedia(previewImage, path);
        }
        Video savedVideo = videoRepository.save(oldVideo);
        VideoModel savedVideoModel = videoMapper.toModel(savedVideo);
        savedVideoModel.setCategories(videoServiceUtils.toCategoryStringList(savedVideo.getCategories()));
        Long idVideo = savedVideoModel.getIdVideo();
        Long countViews = viewedVideoRepository.countViewedVideosByVideo_IdVideo(idVideo);
        savedVideoModel.setCountViewing(countViews);
        Long countLikes = assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, true);
        savedVideoModel.setCountLikes(countLikes);
        Long countDislikes = assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, false);
        savedVideoModel.setCountDislikes(countDislikes);
        return savedVideoModel;
    }

    public void deleteVideo(Long id) {
        String videoPath = "video\\" + id + ".mp4";
        String previewPath = "previewVideo\\" + id + ".jpeg";
        videoRepository.deleteById(id);
        mediaService.deleteMedia(videoPath);
        mediaService.deleteMedia(previewPath);
    }

    public VideoModel findVideoById(Long id) {
        Video video = videoRepository.findById(id).orElseThrow(() -> new NotFoundException("Video not found"));
        Long countViews = viewedVideoRepository.countViewedVideosByVideo_IdVideo(id);
        Long countLikes = assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(id, true);
        Long countDislikes = assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(id, false);
        VideoModel videoModel = videoMapper.toModel(video);
        videoModel.setCountViewing(countViews);
        videoModel.setCountLikes(countLikes);
        videoModel.setCountDislikes(countDislikes);
        videoModel.setCategories(videoServiceUtils.toCategoryStringList(video.getCategories()));
        return videoModel;
    }

    public List<VideoModel> getSubscriptionsVideos(Long idUser) {
        List<Video> videos = videoRepository.getVideosByUser_IdUser(idUser);
        List<VideoModel> videoModels = videoServiceUtils.getVideoModelListWithCategories(videos);
        return videoServiceUtils.addFieldInModelList(videoModels);
    }

    public List<ViewedVideoModel> getViewedVideos(Long idUser) {
        List<ViewedVideo> viewedVideos = viewedVideoRepository.getViewedVideosByIdUser(idUser);
        List<ViewedVideoModel> viewedVideoModels = viewedVideoMapper.toModelList(viewedVideos);
        for (ViewedVideoModel viewedVideoModel : viewedVideoModels) {
            Long idVideo = viewedVideoModel.getVideo().getIdVideo();
            Long countViews = viewedVideoRepository.countViewedVideosByVideo_IdVideo(idVideo);
            Long countLikes = assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, true);
            Long countDislikes = assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, false);
            viewedVideoModel.getVideo().setCountViewing(countViews);
            viewedVideoModel.getVideo().setCountLikes(countLikes);
            viewedVideoModel.getVideo().setCountDislikes(countDislikes);
        }
        Iterator<ViewedVideo> viewedVideoIterator = viewedVideos.iterator();
        Iterator<ViewedVideoModel> viewedVideoModelIterator = viewedVideoModels.iterator();
        while (viewedVideoIterator.hasNext() && viewedVideoModelIterator.hasNext()) {
            List<Category> categories = viewedVideoIterator.next().getVideo().getCategories();
            viewedVideoModelIterator.next().getVideo().setCategories(videoServiceUtils.toCategoryStringList(categories));
        }
        return viewedVideoModels;
    }

    public List<VideoModel> getVideosByName(String name) {
        List<Video> videos = videoRepository.findByNameContaining(name);
        List<VideoModel> videoModels = videoServiceUtils.getVideoModelListWithCategories(videos);
        return videoServiceUtils.addFieldInModelList(videoMapper.toModelList(videos));
    }

    public List<VideoModel> getVideosByUserName(String name) {
        List<Video> videos = videoRepository.findByUser_ChannelNameContaining(name);
        List<VideoModel> videoModels = videoServiceUtils.getVideoModelListWithCategories(videos);
        return videoServiceUtils.addFieldInModelList(videoModels);
    }

    public List<VideoModel> getVideosByCategories(List<String> categories) {
        List<Video> videos = videoRepository.findDistinctByCategories_NameIn(categories);
        List<VideoModel> videoModels = videoServiceUtils.getVideoModelListWithCategories(videos);
        return videoServiceUtils.addFieldInModelList(videoModels);
    }

    public VideoModel insertAssessmentVideo(AssessmentVideoModel assessmentVideoModel) {
        AssessmentVideo assessmentVideo = assessmentVideoMapper.toEntity(assessmentVideoModel);
        assessmentVideo.setDateOfAssessment(Timestamp.valueOf(LocalDateTime.now()));
        assessmentVideoRepository.save(assessmentVideo);
        return findVideoById(assessmentVideoModel.getVideo().getIdVideo());
    }

    public VideoModel deleteAssessmentVideo(Long idUser, Long idVideo) {
        Long id = assessmentVideoRepository.getAssessmentVideoByIdUserAndVideo_IdVideo(idUser, idVideo);
        assessmentVideoRepository.deleteById(id);
        return findVideoById(idVideo);
    }

    public VideoModel insertViewedVideo(ViewedVideoModel viewedVideoModel) {
        ViewedVideo viewedVideo = viewedVideoMapper.toEntity(viewedVideoModel);
        viewedVideo.setDateOfViewing(Timestamp.valueOf(LocalDateTime.now()));
        viewedVideoRepository.save(viewedVideo);
        return findVideoById(viewedVideoModel.getVideo().getIdVideo());
    }

    public List<VideoModel> getLikedVideos(Long idUser) {
        List<AssessmentVideo> assessmentVideos = assessmentVideoRepository.getAssessmentVideoByIdUserAndLiked(idUser, true);
        List<Video> videos = new ArrayList<>();
        assessmentVideos
                .forEach(assessmentVideo -> videos.add(assessmentVideo.getVideo()));
        List<VideoModel> videoModels = videoServiceUtils.getVideoModelListWithCategories(videos);
        return videoServiceUtils.addFieldInModelList(videoModels);
    }

    public List<PlaylistWithVideosModel> getVideosFromPlaylist(Long idPlaylist) {
        List<PlaylistWithVideos> playlistWithVideos =
                playlistWithVideosRepository.getPlaylistWithVideosByIdPlaylist(idPlaylist);
        List<PlaylistWithVideosModel> playlistWithVideosModels =
                playlistWithVideosMapper.toModelList(playlistWithVideos);
        Iterator<PlaylistWithVideos> playlistWithVideosIterator = playlistWithVideos.iterator();
        Iterator<PlaylistWithVideosModel> playlistWithVideosModelIterator = playlistWithVideosModels.iterator();
        while (playlistWithVideosModelIterator.hasNext() && playlistWithVideosIterator.hasNext()) {
            VideoModel videoModel = playlistWithVideosModelIterator.next().getVideo();
            Long idVideoModel = videoModel.getIdVideo();
            List<Category> categories = playlistWithVideosIterator.next().getVideo().getCategories();
            List<String> categoryStrings = videoServiceUtils.toCategoryStringList(categories);
            videoModel.setCategories(categoryStrings);
            Long countViews = viewedVideoRepository.countViewedVideosByVideo_IdVideo(idVideoModel);
            Long countLikes = assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideoModel, true);
            Long countDislikes = assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideoModel, false);
            videoModel.setCountViewing(countViews);
            videoModel.setCountLikes(countLikes);
            videoModel.setCountDislikes(countDislikes);
        }
        return playlistWithVideosModels;
    }

    public List<PlaylistWithVideosModel> insertPlaylistWithVideos(PlaylistWithVideosModel playlistWithVideosModel) {
        PlaylistWithVideos playlistWithVideos = playlistWithVideosMapper.toEntity(playlistWithVideosModel);
        playlistWithVideos.setDateOfAddition(Timestamp.valueOf(LocalDateTime.now()));
        playlistWithVideosRepository.save(playlistWithVideos);
        return getVideosFromPlaylist(playlistWithVideosModel.getIdPlaylist());
    }

    public List<PlaylistWithVideosModel> deletePlaylistWithVideos(Long idPlaylist, Long idVideo) {
        Long id = playlistWithVideosRepository
                .getPlaylistWithVideosByIdPlaylistAndVideo_IdVideo(idPlaylist, idVideo);
        playlistWithVideosRepository.deleteById(id);
        return getVideosFromPlaylist(idPlaylist);
    }
}


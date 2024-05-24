package com.example.videohosting.service;

import com.example.videohosting.entity.AssessmentVideo;
import com.example.videohosting.entity.Category;
import com.example.videohosting.entity.Playlist;
import com.example.videohosting.entity.PlaylistWithVideos;
import com.example.videohosting.entity.User;
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
import com.example.videohosting.model.UserModel;
import com.example.videohosting.model.VideoModel;
import com.example.videohosting.model.ViewedVideoModel;
import com.example.videohosting.repository.AssessmentVideoRepository;
import com.example.videohosting.repository.PlaylistWithVideosRepository;
import com.example.videohosting.repository.VideoRepository;
import com.example.videohosting.repository.ViewedVideoRepository;
import org.jcodec.api.JCodecException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class VideoServiceTest {
    @Mock
    private VideoRepository videoRepository;

    @Mock
    private VideoMapper videoMapper;

    @Mock
    private ViewedVideoRepository viewedVideoRepository;

    @Mock
    private ViewedVideoMapper viewedVideoMapper;

    @Mock
    private AssessmentVideoRepository assessmentVideoRepository;

    @Mock
    private AssessmentVideoMapper assessmentVideoMapper;

    @Mock
    private PlaylistWithVideosRepository playlistWithVideosRepository;

    @Mock
    private PlaylistWithVideosMapper playlistWithVideosMapper;

    @Mock
    private MediaService mediaService;
    @Mock
    private MultipartFile videoFile;
    @Mock
    private MultipartFile previewImage;
    @Mock
    private VideoServiceUtils videoServiceUtils;

    @InjectMocks
    private VideoService videoService;

    private VideoModel videoModel;
    private Video video;
    private User user;
    private UserModel userModel;
    private final String pathPreview = "previewVideo\\1.jpeg";
    private final String videoPath = "video\\1.mp4";
    private List<Video> videos;
    private List<VideoModel> videoModels;
    private Playlist playlist;
    private PlaylistWithVideos playlistWithVideo;
    private PlaylistWithVideosModel playlistWithVideosModel;
    private List<PlaylistWithVideos> playlistWithVideos;
    private List<PlaylistWithVideosModel> playlistWithVideosModels;

    @BeforeEach
    public void setUp() {
        //MockitoAnnotations.openMocks(this);
        Timestamp dateTime = Timestamp.valueOf(LocalDateTime.now());

        user = new User();
        user.setIdUser(1L);
        user.setEmail("user@example.com");
        user.setChannelName("User Channel");
        user.setDescription("User Description");
        user.setDateOfRegistration(dateTime);
        user.setPassword("password");
        user.setSubscriptions(new ArrayList<>());
        user.setVideos(new ArrayList<>());
        user.setPlaylists(new ArrayList<>());

        userModel = new UserModel();
        userModel.setIdUser(1L);
        userModel.setEmail("user@example.com");
        userModel.setChannelName("User Channel");
        userModel.setDescription("User Description");
        userModel.setDateOfRegistration(dateTime);
        userModel.setPassword("password");
        userModel.setSubscriptions(new ArrayList<>());
        userModel.setVideos(new ArrayList<>());
        userModel.setPlaylists(new ArrayList<>());

        video = new Video();
        video.setIdVideo(1L);
        video.setUser(user);
        video.setName("Video");
        video.setDuration(1200L);
        video.setDescription("Description for Video");
        video.setReleaseDateTime(dateTime);
        video.setCategories(new ArrayList<>());

        videoModel = new VideoModel();
        videoModel.setIdVideo(1L);
        videoModel.setUser(userModel);
        videoModel.setName("Video");
        videoModel.setDuration(1200L);
        videoModel.setDescription("Description for Video");
        videoModel.setReleaseDateTime(dateTime);
        videoModel.setCategories(new ArrayList<>());
        videoModel.setCountViewing(0L);
        videoModel.setCountLikes(0L);
        videoModel.setCountDislikes(0L);

    }

    private void createAndSaveSubscription() {
        User subscription = new User();
        UserModel userSubscriptionModel = new UserModel();

        Video videoSubscription = new Video();
        videoSubscription.setIdVideo(2L);
        videoSubscription.setUser(subscription);

        VideoModel videoSubscriptionModel = new VideoModel();
        videoSubscriptionModel.setIdVideo(2L);
        videoSubscriptionModel.setUser(userSubscriptionModel);

        videos = Arrays.asList(videoSubscription);
        videoModels = Arrays.asList(videoSubscriptionModel);

        subscription.setIdUser(1L);
        subscription.setVideos(videos);

        userSubscriptionModel.setIdUser(1L);
        userSubscriptionModel.setVideos(videoModels);


        user.setSubscriptions(Arrays.asList(subscription));
        userModel.setSubscriptions(Arrays.asList(userSubscriptionModel));

    }

    private void createAndSavePlaylist() {
        Long idPlaylist = 1L;
        playlist = new Playlist();
        playlist.setIdPlaylist(idPlaylist);
        playlistWithVideos = new ArrayList<>();
        playlistWithVideo = new PlaylistWithVideos();
        playlistWithVideo.setIdPlaylistWithVideos(1L);
        playlistWithVideo.setVideo(video);
        playlistWithVideo.setIdPlaylist(idPlaylist);
        playlistWithVideos.add(playlistWithVideo);

        playlistWithVideosModels = new ArrayList<>();
        playlistWithVideosModel = new PlaylistWithVideosModel();
        playlistWithVideosModel.setIdPlaylistWithVideos(1L);
        playlistWithVideosModel.setIdPlaylist(idPlaylist);
        playlistWithVideosModel.setVideo(videoModel);
        playlistWithVideosModels.add(playlistWithVideosModel);
    }

    @Test
    void insertVideo() throws JCodecException, IOException {
        when(videoMapper.toEntity(videoModel)).thenReturn(video);
        when(videoRepository.save(video)).thenReturn(video);
        when(videoMapper.toModel(video)).thenReturn(videoModel);
        when(mediaService.getDuration(videoPath)).thenReturn(1200L);
        when(videoServiceUtils.toCategoryEntityList(videoModel.getCategories())).thenReturn(new ArrayList<>());
        when(videoServiceUtils.toCategoryStringList(video.getCategories())).thenReturn(new ArrayList<>());
        VideoModel savedVideoModel = videoService.insertVideo(videoModel, videoFile, previewImage);
        assertEquals(videoModel, savedVideoModel);
        verify(mediaService).saveMedia(previewImage, pathPreview);
        verify(videoRepository, times(2)).save(video);
        verify(mediaService).saveMedia(videoFile, videoPath);
        verify(mediaService).getDuration(videoPath);
    }

    @Test
    void insertVideoNegativeTest() {
        videoFile = null;
        assertThrows(LoadFileException.class,
                () -> videoService.insertVideo(videoModel, videoFile, previewImage));
    }


    @Test
    void updateVideo() {
        Long id = video.getIdVideo();
        when(videoRepository.findById(id)).thenReturn(Optional.of(video));
        when(videoMapper.toEntity(videoModel)).thenReturn(video);
        when(videoRepository.save(video)).thenReturn(video);
        when(videoMapper.toModel(video)).thenReturn(videoModel);
        when(videoServiceUtils.toCategoryStringList(video.getCategories())).thenReturn(new ArrayList<>());
        when(viewedVideoRepository.countViewedVideosByVideo_IdVideo(id)).thenReturn(0L);
        when(assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(id, true)).thenReturn(0L);
        when(assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(id, false)).thenReturn(0L);

        VideoModel updatedVideoModel = videoService.updateVideo(videoModel, previewImage);

        assertEquals(videoModel, updatedVideoModel);
        verify(mediaService).saveMedia(previewImage, pathPreview);
    }

    @Test
    void updateVideoWithNullPreview() {
        Long id = video.getIdVideo();
        previewImage = null;
        when(videoRepository.findById(id)).thenReturn(Optional.of(video));
        when(videoMapper.toEntity(videoModel)).thenReturn(video);
        when(videoRepository.save(video)).thenReturn(video);
        when(videoMapper.toModel(video)).thenReturn(videoModel);
        when(videoServiceUtils.toCategoryStringList(video.getCategories())).thenReturn(new ArrayList<>());
        when(viewedVideoRepository.countViewedVideosByVideo_IdVideo(id)).thenReturn(0L);
        when(assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(id, true)).thenReturn(0L);
        when(assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(id, false)).thenReturn(0L);

        VideoModel updatedVideoModel = videoService.updateVideo(videoModel, previewImage);

        assertEquals(videoModel, updatedVideoModel);
        verify(mediaService, never()).saveMedia(previewImage, pathPreview);
    }


    @Test
    void deleteVideo() {
        Long id = video.getIdVideo();
        videoService.deleteVideo(id);
        verify(videoRepository).deleteById(id);
        verify(mediaService).deleteMedia(videoPath);
        verify(mediaService).deleteMedia(pathPreview);
    }

    @Test
    void findVideoById() {
        Long id = video.getIdVideo();
        when(videoRepository.findById(id)).thenReturn(Optional.of(video));
        when(viewedVideoRepository.countViewedVideosByVideo_IdVideo(id)).thenReturn(0L);
        when(assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(id, true)).thenReturn(0L);
        when(assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(id, false)).thenReturn(0L);
        when(videoMapper.toModel(video)).thenReturn(videoModel);
        when(videoServiceUtils.toCategoryStringList(video.getCategories())).thenReturn(new ArrayList<>());
        VideoModel foundVideoModel = videoService.findVideoById(id);

        assertEquals(videoModel, foundVideoModel);
    }

    @Test
    void findVideoByIdNegativeTest() {
        Long id = video.getIdVideo();
        when(videoRepository.findById(id)).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> videoService.findVideoById(id));
    }


    @Test
    void getSubscriptionsVideos() {
        createAndSaveSubscription();
        Long id = video.getUser().getIdUser();
        when(videoRepository.getVideosByUser_IdUser(id)).thenReturn(videos);
        when(videoServiceUtils.getVideoModelListWithCategories(videos)).thenReturn(videoModels);
        when(videoServiceUtils.addFieldInModelList(videoModels)).thenReturn(videoModels);

        List<VideoModel> subscriptionVideos = videoService.getSubscriptionsVideos(id);

        assertEquals(videoModels, subscriptionVideos);
    }

    @Test
    void getViewedVideos() {
        Long idUser = video.getUser().getIdUser();
        Long idVideo = video.getIdVideo();
        List<ViewedVideo> viewedVideos = new ArrayList<>();
        ViewedVideo viewedVideo = new ViewedVideo();
        viewedVideo.setIdUser(idUser);
        viewedVideo.setVideo(video);
        viewedVideos.add(viewedVideo);

        List<ViewedVideoModel> viewedVideoModels = new ArrayList<>();
        ViewedVideoModel viewedVideoModel = new ViewedVideoModel();
        viewedVideoModel.setVideo(videoModel);
        viewedVideoModel.setIdUser(idUser);
        viewedVideoModels.add(viewedVideoModel);

        when(viewedVideoRepository.getViewedVideosByIdUser(idUser)).thenReturn(viewedVideos);
        when(viewedVideoMapper.toModelList(viewedVideos)).thenReturn(viewedVideoModels);
        when(viewedVideoRepository.countViewedVideosByVideo_IdVideo(idVideo)).thenReturn(0L);
        when(assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, true)).thenReturn(0L);
        when(assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, false)).thenReturn(0L);
        when(videoServiceUtils.toCategoryStringList(video.getCategories())).thenReturn(new ArrayList<>());

        List<ViewedVideoModel> result = videoService.getViewedVideos(idUser);

        assertEquals(viewedVideoModels, result);
    }

    @Test
    void getVideosByName() {
        videos = new ArrayList<>();
        videos.add(video);
        videoModels = new ArrayList<>();
        videoModels.add(videoModel);

        when(videoRepository.findByNameContaining(video.getName())).thenReturn(videos);
        when(videoServiceUtils.getVideoModelListWithCategories(videos)).thenReturn(videoModels);
        when(videoServiceUtils.addFieldInModelList(videoModels)).thenReturn(videoModels);

        List<VideoModel> result = videoService.getVideosByName(video.getName());

        assertEquals(videoModels, result);
    }

    @Test
    void getVideosByUserName() {
        videos = new ArrayList<>();
        videos.add(video);
        videoModels = new ArrayList<>();
        videoModels.add(videoModel);
        String userName = video.getUser().getChannelName();

        when(videoRepository.findByNameContaining(userName)).thenReturn(videos);
        when(videoServiceUtils.getVideoModelListWithCategories(videos)).thenReturn(videoModels);
        when(videoServiceUtils.addFieldInModelList(videoModels)).thenReturn(videoModels);

        List<VideoModel> result = videoService.getVideosByName(userName);

        assertEquals(videoModels, result);
    }

    @Test
    void getVideosByCategories() {
        String categoryStr = "Cats";
        Category category = new Category();
        category.setName(categoryStr);
        List<String> categoryStrings = List.of(categoryStr);
        List<Category> categories = List.of(category);
        videoModel.setCategories(categoryStrings);
        video.setCategories(categories);
        videos = new ArrayList<>();
        videos.add(video);
        videoModels = new ArrayList<>();
        videoModels.add(videoModel);

        when(videoRepository.findDistinctByCategories_NameIn(categoryStrings)).thenReturn(videos);
        when(videoServiceUtils.getVideoModelListWithCategories(videos)).thenReturn(videoModels);
        when(videoServiceUtils.addFieldInModelList(videoModels)).thenReturn(videoModels);

        List<VideoModel> result = videoService.getVideosByCategories(categoryStrings);

        assertEquals(videoModels, result);
    }

    @Test
    void insertAssessmentVideo() {
        Long id = video.getIdVideo();
        AssessmentVideo assessmentVideo = new AssessmentVideo();
        assessmentVideo.setVideo(video);

        AssessmentVideoModel assessmentVideoModel = new AssessmentVideoModel();
        assessmentVideoModel.setVideo(videoModel);

        when(assessmentVideoMapper.toEntity(assessmentVideoModel)).thenReturn(assessmentVideo);
        when(assessmentVideoRepository.save(assessmentVideo)).thenReturn(assessmentVideo);
        when(videoRepository.findById(id)).thenReturn(Optional.of(video));
        when(videoMapper.toModel(video)).thenReturn(videoModel);

        VideoModel returnedVideoModel = videoService.insertAssessmentVideo(assessmentVideoModel);

        assertEquals(videoModel, returnedVideoModel);
        verify(assessmentVideoRepository).save(assessmentVideo);
    }

    @Test
    void deleteAssessmentVideo() {
        Long id = 1L;
        Long idUser = video.getUser().getIdUser();
        Long idVideo = video.getIdVideo();

        when(assessmentVideoRepository.getAssessmentVideoByIdUserAndVideo_IdVideo(idUser, idVideo)).thenReturn(id);
        when(videoRepository.findById(id)).thenReturn(Optional.of(video));
        when(videoMapper.toModel(video)).thenReturn(videoModel);


        VideoModel returnedVideoModel = videoService.deleteAssessmentVideo(idUser, idVideo);

        assertEquals(videoModel, returnedVideoModel);
        verify(assessmentVideoRepository).deleteById(id);
    }

    @Test
    void insertViewedVideo() {
        Long id = video.getIdVideo();
        ViewedVideo viewedVideo = new ViewedVideo();
        viewedVideo.setVideo(video);

        ViewedVideoModel viewedVideoModel = new ViewedVideoModel();
        viewedVideoModel.setVideo(videoModel);

        when(viewedVideoMapper.toEntity(viewedVideoModel)).thenReturn(viewedVideo);
        when(viewedVideoRepository.save(viewedVideo)).thenReturn(viewedVideo);
        when(videoRepository.findById(id)).thenReturn(Optional.of(video));
        when(videoMapper.toModel(video)).thenReturn(videoModel);

        VideoModel returnedVideoModel = videoService.insertViewedVideo(viewedVideoModel);

        assertEquals(videoModel, returnedVideoModel);
        verify(viewedVideoRepository).save(viewedVideo);
    }

    @Test
    void getLikedVideos() {
        Long idUser = video.getUser().getIdUser();
        List<AssessmentVideo> assessmentVideos = new ArrayList<>();
        AssessmentVideo assessmentVideo = new AssessmentVideo();
        assessmentVideo.setVideo(video);
        assessmentVideo.setIdUser(idUser);
        assessmentVideo.setLiked(true);
        assessmentVideos.add(assessmentVideo);

        when(assessmentVideoRepository.getAssessmentVideoByIdUserAndLiked(idUser, true)).thenReturn(assessmentVideos);
        when(videoServiceUtils.getVideoModelListWithCategories(any(List.class))).thenReturn(videoModels);
        when(videoServiceUtils.addFieldInModelList(videoModels)).thenReturn(videoModels);

        List<VideoModel> likedVideos = videoService.getLikedVideos(idUser);

        assertEquals(videoModels, likedVideos);
    }

    @Test
    void getVideosFromPlaylist() {
        createAndSavePlaylist();
        Long idVideo = video.getIdVideo();
        Long idPlaylist = playlist.getIdPlaylist();
        when(playlistWithVideosRepository.getPlaylistWithVideosByIdPlaylist(idPlaylist)).thenReturn(playlistWithVideos);
        when(playlistWithVideosMapper.toModelList(playlistWithVideos)).thenReturn(playlistWithVideosModels);
        when(viewedVideoRepository.countViewedVideosByVideo_IdVideo(idVideo)).thenReturn(0L);
        when(assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, true)).thenReturn(0L);
        when(assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, false)).thenReturn(0L);
        when(videoServiceUtils.toCategoryStringList(video.getCategories())).thenReturn(new ArrayList<>());

        List<PlaylistWithVideosModel> result = videoService.getVideosFromPlaylist(idPlaylist);

        assertEquals(playlistWithVideosModels, result);
    }

    @Test
    void insertPlaylistWithVideos() {
        createAndSavePlaylist();
        Long idVideo = video.getIdVideo();
        Long idPlaylist = playlist.getIdPlaylist();

        when(playlistWithVideosMapper.toEntity(playlistWithVideosModel)).thenReturn(playlistWithVideo);
        when(playlistWithVideosRepository.save(playlistWithVideo)).thenReturn(playlistWithVideo);
        when(playlistWithVideosRepository.getPlaylistWithVideosByIdPlaylist(idPlaylist)).thenReturn(playlistWithVideos);
        when(playlistWithVideosMapper.toModelList(playlistWithVideos)).thenReturn(playlistWithVideosModels);
        when(viewedVideoRepository.countViewedVideosByVideo_IdVideo(idVideo)).thenReturn(0L);
        when(assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, true)).thenReturn(0L);
        when(assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, false)).thenReturn(0L);
        when(videoServiceUtils.toCategoryStringList(video.getCategories())).thenReturn(new ArrayList<>());

        List<PlaylistWithVideosModel> result = videoService.insertPlaylistWithVideos(playlistWithVideosModel);

        assertEquals(playlistWithVideosModels, result);
        verify(playlistWithVideosRepository).save(playlistWithVideo);
    }

    @Test
    void deletePlaylistWithVideos() {
        createAndSavePlaylist();
        Long id = playlistWithVideo.getIdPlaylistWithVideos();
        Long idVideo = video.getIdVideo();
        Long idPlaylist = playlist.getIdPlaylist();

        when(playlistWithVideosRepository.getPlaylistWithVideosByIdPlaylistAndVideo_IdVideo(idPlaylist, idVideo)).thenReturn(id);
        when(playlistWithVideosRepository.getPlaylistWithVideosByIdPlaylist(idPlaylist)).thenReturn(playlistWithVideos);
        when(playlistWithVideosMapper.toModelList(playlistWithVideos)).thenReturn(playlistWithVideosModels);
        when(viewedVideoRepository.countViewedVideosByVideo_IdVideo(idVideo)).thenReturn(0L);
        when(assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, true)).thenReturn(0L);
        when(assessmentVideoRepository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, false)).thenReturn(0L);
        when(videoServiceUtils.toCategoryStringList(video.getCategories())).thenReturn(new ArrayList<>());

        List<PlaylistWithVideosModel> result = videoService.deletePlaylistWithVideos(1L, 1L);

        assertEquals(playlistWithVideosModels, result);
        verify(playlistWithVideosRepository).deleteById(id);
    }
}
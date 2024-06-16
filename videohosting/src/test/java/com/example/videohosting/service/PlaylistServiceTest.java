package com.example.videohosting.service;

import com.example.videohosting.entity.Playlist;
import com.example.videohosting.entity.User;
import com.example.videohosting.exception.NotFoundException;
import com.example.videohosting.mapper.PlaylistMapper;
import com.example.videohosting.model.PlaylistModel;
import com.example.videohosting.model.UserModel;
import com.example.videohosting.repository.PlaylistRepository;
import com.example.videohosting.repository.PlaylistWithVideosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {
    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private PlaylistMapper playlistMapper;

    @Mock
    private PlaylistWithVideosRepository playlistWithVideosRepository;

    @Mock
    private MediaService mediaService;

    @InjectMocks
    private PlaylistService playlistService;

    private PlaylistModel playlistModel;
    private Playlist playlist;


    @BeforeEach
    public void setUp() {
        //MockitoAnnotations.openMocks(this);

        Timestamp dateTime = Timestamp.valueOf(LocalDateTime.now());

        UserModel userModel = new UserModel();
        userModel.setIdUser(1L);
        userModel.setEmail("test@example.com");
        userModel.setChannelName("Test Channel");
        userModel.setDescription("Test Description");
        userModel.setDateOfRegistration(dateTime);
        userModel.setPassword("password");

        User user = new User();
        user.setIdUser(1L);
        user.setEmail("test@example.com");
        user.setChannelName("Test Channel");
        user.setDescription("Test Description");
        user.setDateOfRegistration(dateTime);
        user.setPassword("password");

        playlistModel = new PlaylistModel();
        playlistModel.setIdPlaylist(1L);
        playlistModel.setNamePlaylist("Test Playlist");
        playlistModel.setDateCreation(dateTime);
        playlistModel.setUser(userModel);
        playlistModel.setCountVideos(0L);

        playlist = new Playlist();
        playlist.setIdPlaylist(1L);
        playlist.setNamePlaylist("Test Playlist");
        playlist.setDateCreation(dateTime);
        playlist.setUser(user);
    }

    @Test
    void insert() {
        String path = "imageIconPlaylist\\" + playlist.getIdPlaylist() + ".jpeg";
        MultipartFile icon = mock(MultipartFile.class);
        when(playlistMapper.toEntity(playlistModel)).thenReturn(playlist);
        when(playlistRepository.save(playlist)).thenReturn(playlist);
        when(playlistMapper.toModel(playlist)).thenReturn(playlistModel);
        doNothing().when(mediaService).saveMedia(icon, path);

        PlaylistModel result = playlistService.insert(playlistModel, icon);

        assertEquals(playlistModel, result);
        verify(mediaService).saveMedia(icon, path);
    }

    @Test
    void update() {
        Long idPlaylist = playlist.getIdPlaylist();
        MultipartFile icon = mock(MultipartFile.class);
        String path = "imageIconPlaylist\\" + playlist.getIdPlaylist() + ".jpeg";
        when(playlistMapper.toEntity(playlistModel)).thenReturn(playlist);
        when(playlistRepository.findById(idPlaylist)).thenReturn(Optional.of(playlist));
        when(playlistRepository.save(playlist)).thenReturn(playlist);
        when(playlistMapper.toModel(playlist)).thenReturn(playlistModel);
        when(playlistWithVideosRepository.countPlaylistWithVideosByIdPlaylist(idPlaylist)).thenReturn(0L);
        doNothing().when(mediaService).saveMedia(icon, path);

        PlaylistModel result = playlistService.update(playlistModel, icon);

        assertEquals(playlistModel, result);
        verify(mediaService).saveMedia(icon, path);
    }

    @Test
    void updateNegativeTest() {
        Long idPlaylist = playlist.getIdPlaylist();
        String path = "imageIconPlaylist\\" + playlist.getIdPlaylist() + ".jpeg";
        when(playlistMapper.toEntity(playlistModel)).thenReturn(playlist);
        when(playlistRepository.findById(idPlaylist)).thenReturn(Optional.of(playlist));
        when(playlistRepository.save(playlist)).thenReturn(playlist);
        when(playlistMapper.toModel(playlist)).thenReturn(playlistModel);
        when(playlistWithVideosRepository.countPlaylistWithVideosByIdPlaylist(idPlaylist)).thenReturn(0L);

        PlaylistModel result = playlistService.update(playlistModel, null);

        assertEquals(playlistModel, result);
        verify(mediaService, never()).saveMedia(null, path);
    }


    @Test
    void delete() {
        Long id = 1L;
        String path = "imageIconPlaylist\\" + id + ".jpeg";
        playlistService.delete(id);

        verify(playlistRepository).deleteById(id);
        verify(mediaService).deleteMedia(path);
    }

    @Test
    void findPlaylistById() {
        Long id = playlist.getIdPlaylist();
        when(playlistRepository.findById(id)).thenReturn(Optional.of(playlist));
        when(playlistMapper.toModel(playlist)).thenReturn(playlistModel);
        when(playlistWithVideosRepository.countPlaylistWithVideosByIdPlaylist(id)).thenReturn(0L);

        PlaylistModel result = playlistService.findPlaylistById(id);

        assertEquals(playlistModel, result);
    }

    @Test
    void findPlaylistByIdNegativeTest() {
        Long id = 3L;
        when(playlistRepository.findById(id)).thenThrow(NotFoundException.class);

        verify(playlistMapper, never()).toModel(any(Playlist.class));
        verify(playlistWithVideosRepository,never()).countPlaylistWithVideosByIdPlaylist(id);
        assertThrows(NotFoundException.class, () -> playlistService.findPlaylistById(id));
    }

}
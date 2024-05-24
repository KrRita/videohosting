package com.example.videohosting.service;

import com.example.videohosting.entity.User;
import com.example.videohosting.mapper.userMapper.UserMapper;
import com.example.videohosting.model.UserModel;
import com.example.videohosting.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
    private User user;
    private UserModel userModel;

    @BeforeEach
    public void setUp() {
        //MockitoAnnotations.openMocks(this);
        Timestamp date = Timestamp.valueOf(LocalDateTime.now());

        user = new User();
        user.setIdUser(1L);
        user.setEmail("testuser@example.com");
        user.setChannelName("Test Channel");
        user.setDescription("Test Description");
        user.setDateOfRegistration(date);
        user.setPassword("password");
        user.setSubscriptions(List.of());
        user.setVideos(List.of());
        user.setPlaylists(List.of());

        userModel = new UserModel();
        userModel.setIdUser(1L);
        userModel.setEmail("testuser@example.com");
        userModel.setChannelName("Test Channel");
        userModel.setDescription("Test Description");
        userModel.setDateOfRegistration(date);
        userModel.setPassword("password");
        userModel.setSubscriptions(List.of());
        userModel.setVideos(List.of());
        userModel.setPlaylists(List.of());
        userModel.setCountSubscribers(0L);
    }

    @Test
    void loadUserByUsername() {
        String email = userModel.getEmail();
        when(userRepository.getUserByEmail(email)).thenReturn(user);
        when(userMapper.toModel(user)).thenReturn(userModel);
        UserModel result = customUserDetailsService.loadUserByUsername(email);
        assertEquals(userModel, result);
    }

    @Test
    void loadUserByUsernameNegativeTest() {
        String email = "cat@example.com";
        when(userRepository.getUserByEmail(email)).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(email));
    }

}
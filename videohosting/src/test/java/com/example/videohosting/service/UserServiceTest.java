package com.example.videohosting.service;

import com.example.videohosting.entity.User;
import com.example.videohosting.exception.NotFoundException;
import com.example.videohosting.exception.UserAlreadyExistsException;
import com.example.videohosting.mapper.userMapper.UserMapper;
import com.example.videohosting.model.UserModel;
import com.example.videohosting.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;
    @Mock
    private MediaService mediaService;
    @Mock
    private MultipartFile imageIcon;
    @Mock
    private MultipartFile imageHeader;
    @Mock
    private PasswordEncoder passwordEncoder;
    private User user;
    private UserModel userModel;
    private final String pathIcon = "imageIconUser\\1.jpeg";
    private final String pathHeader = "imageHeaderUser\\1.jpeg";

    @BeforeEach
    public void setUp() {
        //MockitoAnnotations.openMocks(this);

        Timestamp dateTime = Timestamp.valueOf(LocalDateTime.now());

        userModel = new UserModel();
        userModel.setIdUser(1L);
        userModel.setEmail("test@example.com");
        userModel.setChannelName("Test Channel");
        userModel.setDescription("Test Description");
        userModel.setDateOfRegistration(dateTime);
        userModel.setPassword("password");
        userModel.setCountSubscribers(0L);

        user = new User();
        user.setIdUser(1L);
        user.setEmail("test@example.com");
        user.setChannelName("Test Channel");
        user.setDescription("Test Description");
        user.setDateOfRegistration(dateTime);
        user.setPassword("password");
    }

    @Test
    void insert() {
        String email = user.getEmail();
        when(userRepository.getUserByEmail(email)).thenReturn(null);
        when(userMapper.toEntity(userModel)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toModel(user)).thenReturn(userModel);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(any(String.class));

        UserModel result = userService.insert(userModel, imageHeader, imageIcon);

        assertEquals(userModel, result);
        verify(mediaService).saveMedia(imageIcon, pathIcon);
        verify(mediaService).saveMedia(imageHeader, pathHeader);
    }

    @Test
    void insertNegativeTest() {
        String email = userModel.getEmail();
        when(userRepository.getUserByEmail(email)).thenReturn(user);

        assertThrows(UserAlreadyExistsException.class,
                () -> userService.insert(userModel, imageHeader, imageIcon));
    }


    @Test
    void update() {
        Long idUser = user.getIdUser();
        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        when(userMapper.toEntity(userModel)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toModel(user)).thenReturn(userModel);
        when(userRepository.getSubscribersCountByIdUser(idUser)).thenReturn(0L);

        UserModel result = userService.update(userModel, imageHeader, imageIcon);

        assertEquals(userModel, result);
        verify(mediaService).saveMedia(imageHeader, pathHeader);
        verify(mediaService).saveMedia(imageIcon, pathIcon);
    }

    @Test
    void updateWithNulls() {
        Long idUser = user.getIdUser();
        Timestamp dateTime = user.getDateOfRegistration();
        String email = user.getEmail();
        User testUser = new User(idUser, email, null, null, dateTime, null,
                null, null, null);
        UserModel testUserModel = new UserModel(idUser, email, null, null, dateTime,
                null, null, null, null, 0L);
        imageHeader = null;
        imageIcon = null;
        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
        when(userMapper.toEntity(testUserModel)).thenReturn(testUser);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toModel(user)).thenReturn(userModel);
        when(userRepository.getSubscribersCountByIdUser(idUser)).thenReturn(0L);

        userService.update(testUserModel, imageHeader, imageIcon);

        verify(mediaService, never()).saveMedia(imageHeader, pathHeader);
        verify(mediaService, never()).saveMedia(imageIcon, pathIcon);
    }


    @Test
    void addSubscription() {
        Long idUser = user.getIdUser();
        User userTest = new User(idUser, user.getEmail(), user.getChannelName(), user.getDescription(),
                user.getDateOfRegistration(), user.getPassword(), new ArrayList<>(), null, null);
        User subscription = new User();
        subscription.setIdUser(2L);
        UserModel subscriptionModel = new UserModel();
        subscriptionModel.setIdUser(2L);
        List<User> subscriptions = new ArrayList<>();
        subscriptions.add(subscription);
        List<UserModel> subscriptionsModels = new ArrayList<>();
        subscriptionsModels.add(subscriptionModel);
        userModel.setSubscriptions(subscriptionsModels);
        user.setSubscriptions(subscriptions);

        when(userRepository.findById(idUser)).thenReturn(Optional.of(userTest));
        when(userMapper.toEntityList(subscriptionsModels)).thenReturn(subscriptions);
        when(userRepository.save(userTest)).thenReturn(user);
        when(userMapper.toModel(user)).thenReturn(userModel);
        when(userRepository.getSubscribersCountByIdUser(idUser)).thenReturn(0L);

        UserModel result = userService.addSubscription(userModel);

        assertEquals(userModel, result);
    }

    @Test
    void deleteSubscription() {
        Long idUser = user.getIdUser();
        User subscription = new User();
        subscription.setIdUser(2L);
        UserModel subscriptionModel = new UserModel();
        subscriptionModel.setIdUser(2L);
        User userTest = new User(idUser, user.getEmail(), user.getChannelName(), user.getDescription(),
                user.getDateOfRegistration(), user.getPassword(), List.of(), null, null);
        UserModel userModelTest = new UserModel(idUser, user.getEmail(), user.getChannelName(), user.getDescription(),
                user.getDateOfRegistration(), user.getPassword(), List.of(), null, null, 0L);
        List<User> subscriptions = new ArrayList<>();
        subscriptions.add(subscription);
        List<UserModel> subscriptionsModels = new ArrayList<>();
        subscriptionsModels.add(subscriptionModel);
        user.setSubscriptions(subscriptions);
        userModel.setSubscriptions(subscriptionsModels);

        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(userTest);
        when(userMapper.toModel(userTest)).thenReturn(userModelTest);
        when(userRepository.getSubscribersCountByIdUser(idUser)).thenReturn(0L);

        UserModel result = userService.deleteSubscription(userModel);

        assertEquals(userModelTest, result);
    }

    @Test
    void delete() {
        Long id = user.getIdUser();
        userService.delete(id);
        verify(userRepository).deleteById(id);
        verify(mediaService).deleteMedia(pathHeader);
        verify(mediaService).deleteMedia(pathIcon);
    }

    @Test
    void findUserById() {
        Long id = user.getIdUser();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toModel(user)).thenReturn(userModel);
        when(userRepository.getSubscribersCountByIdUser(id)).thenReturn(0L);

        UserModel result = userService.findUserById(1L);
        assertEquals(userModel, result);
    }

    @Test
    void findUserByIdNegativeTest() {
        Long id = user.getIdUser();
        when(userRepository.findById(id)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> userService.findUserById(id));
        verify(userMapper, never()).toModel(user);
        verify(userRepository, never()).getSubscribersCountByIdUser(id);
    }

}
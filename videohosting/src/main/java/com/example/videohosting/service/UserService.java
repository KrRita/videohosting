package com.example.videohosting.service;

import com.example.videohosting.entity.User;
import com.example.videohosting.exception.NotFoundException;
import com.example.videohosting.exception.UserAlreadyExistsException;
import com.example.videohosting.mapper.userMapper.UserMapper;
import com.example.videohosting.model.UserModel;
import com.example.videohosting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MediaService mediaService;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
                       MediaService mediaService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.mediaService = mediaService;
    }

    public UserModel insert(UserModel userModel, MultipartFile imageHeader, MultipartFile imageIcon) {
        User user = userRepository.getUserByEmail(userModel.getEmail());
        if (user != null) {
            throw new UserAlreadyExistsException("User already exists");
        }
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userModel.setDateOfRegistration(Timestamp.valueOf(LocalDateTime.now()));
        User savedUser = userRepository.save(userMapper.toEntity(userModel));
        if (imageIcon != null) {
            String path = "imageIconUser\\" + savedUser.getIdUser() + ".jpeg";
            mediaService.saveMedia(imageIcon, path);
        }
        if (imageHeader != null) {
            String path = "imageHeaderUser\\" + savedUser.getIdUser() + ".jpeg";
            mediaService.saveMedia(imageHeader, path);
        }
        UserModel savedUserModel = userMapper.toModel(savedUser);
        savedUserModel.setCountSubscribers(0L);
        return savedUserModel;
    }

    public UserModel update(UserModel userModel, MultipartFile imageHeader, MultipartFile imageIcon) {
        User oldUser = userRepository.findById(userModel.getIdUser()).orElseThrow(() -> new NotFoundException("User not found"));
        User newUser = userMapper.toEntity(userModel);
        if (newUser.getChannelName() != null) {
            oldUser.setChannelName(newUser.getChannelName());
        }
        if (newUser.getPassword() != null) {
            oldUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
        if (imageHeader != null) {
            String path = "imageHeaderUser\\" + oldUser.getIdUser() + ".jpeg";
            mediaService.saveMedia(imageHeader, path);
        }
        if (imageIcon != null) {
            String path = "imageIconUser\\" + oldUser.getIdUser() + ".jpeg";
            mediaService.saveMedia(imageIcon, path);
        }
        if (newUser.getDescription() != null) {
            oldUser.setDescription(newUser.getDescription());
        }
        User savedUser = userRepository.save(oldUser);
        UserModel savedUserModel = userMapper.toModel(savedUser);
        Long countSubscribers = userRepository.getSubscriptionsCountByIdUser(savedUserModel.getIdUser());
        savedUserModel.setCountSubscribers(countSubscribers);
        return savedUserModel;
    }

    public UserModel addSubscription(UserModel userModel) {
        User oldUser = userRepository.findById(userModel.getIdUser()).orElseThrow(() -> new NotFoundException("User not found"));
        List<User> subscriptions = userMapper.toEntityList(userModel.getSubscriptions());
        oldUser.getSubscriptions().addAll(subscriptions);
        User savedUser = userRepository.save(oldUser);
        UserModel savedUserModel = userMapper.toModel(savedUser);
        Long countSubscribers = userRepository.getSubscriptionsCountByIdUser(savedUserModel.getIdUser());
        savedUserModel.setCountSubscribers(countSubscribers);
        return savedUserModel;
    }

    public UserModel deleteSubscription(UserModel userModel) {
        User oldUser = userRepository.findById(userModel.getIdUser()).orElseThrow(() -> new NotFoundException("User not found"));
        Long idSubscription = userModel.getSubscriptions().get(0).getIdUser();
        oldUser.getSubscriptions().removeIf(user -> user.getIdUser().equals(idSubscription));
        User savedUser = userRepository.save(oldUser);
        UserModel savedUserModel = userMapper.toModel(savedUser);
        Long countSubscribers = userRepository.getSubscriptionsCountByIdUser(savedUserModel.getIdUser());
        savedUserModel.setCountSubscribers(countSubscribers);
        return savedUserModel;
    }


    public void delete(Long id) {
        String iconPath = "imageIconUser\\" + id + ".jpeg";
        String headerPath = "imageHeaderUser\\" + id + ".jpeg";
        userRepository.deleteById(id);
        mediaService.deleteMedia(iconPath);
        mediaService.deleteMedia(headerPath);
    }

    public UserModel findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        UserModel userModel = userMapper.toModel(user);
        Long countSubscribers = userRepository.getSubscriptionsCountByIdUser(id);
        userModel.setCountSubscribers(countSubscribers);
        return userModel;
    }
}

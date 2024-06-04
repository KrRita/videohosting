package com.example.videohosting.service;

import com.example.videohosting.entity.User;
import com.example.videohosting.exception.DeleteFileException;
import com.example.videohosting.exception.NotFoundException;
import com.example.videohosting.exception.UserAlreadyExistsException;
import com.example.videohosting.mapper.userMapper.UserMapper;
import com.example.videohosting.model.UserModel;
import com.example.videohosting.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
                       MediaService mediaService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.mediaService = mediaService;
    }

    @Transactional
    @CachePut(value = "users", key = "#result.idUser")
    public UserModel insert(UserModel userModel, MultipartFile imageHeader, MultipartFile imageIcon) {
        logger.info("Inserting new user: {}", userModel.getEmail());
        User user = userRepository.getUserByEmail(userModel.getEmail());
        if (user != null) {
            logger.error("User with email {} already exists", userModel.getEmail());
            throw new UserAlreadyExistsException("User already exists");
        }
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userModel.setDateOfRegistration(Timestamp.valueOf(LocalDateTime.now()));
        User savedUser = userRepository.save(userMapper.toEntity(userModel));
        if (imageIcon != null) {
            String path = "imageIconUser\\" + savedUser.getIdUser() + ".jpeg";
            mediaService.saveMedia(imageIcon, path);
            logger.info("Saved image icon for user at path: {}", path);
        }
        if (imageHeader != null) {
            String path = "imageHeaderUser\\" + savedUser.getIdUser() + ".jpeg";
            mediaService.saveMedia(imageHeader, path);
            logger.info("Saved image header for user at path: {}", path);
        }
        UserModel savedUserModel = userMapper.toModel(savedUser);
        savedUserModel.setCountSubscribers(0L);
        logger.info("User inserted successfully with id: {}", savedUser.getIdUser());
        return savedUserModel;
    }

    @CachePut(value = "users", key = "#result.idUser")
    public UserModel update(UserModel userModel, MultipartFile imageHeader, MultipartFile imageIcon) {
        logger.info("Updating user with id: {}", userModel.getIdUser());
        User oldUser = userRepository.findById(userModel.getIdUser())
                .orElseThrow(() -> {
                    logger.error("User with id {} not found", userModel.getIdUser());
                    return new NotFoundException("User not found");
                });
        User newUser = userMapper.toEntity(userModel);
        if (newUser.getChannelName() != null) {
            oldUser.setChannelName(newUser.getChannelName());
            logger.info("Updated channel name to: {}", newUser.getChannelName());
        }
        if (newUser.getPassword() != null) {
            oldUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            logger.info("Updated password for user with id: {}", oldUser.getIdUser());
        }
        if (imageHeader != null) {
            String path = "imageHeaderUser\\" + oldUser.getIdUser() + ".jpeg";
            mediaService.saveMedia(imageHeader, path);
            logger.info("Saved image header for user at path: {}", path);
        }
        if (imageIcon != null) {
            String path = "imageIconUser\\" + oldUser.getIdUser() + ".jpeg";
            mediaService.saveMedia(imageIcon, path);
            logger.info("Saved image icon for user at path: {}", path);
        }
        if (newUser.getDescription() != null) {
            oldUser.setDescription(newUser.getDescription());
            logger.info("Updated description for user with id: {}", oldUser.getIdUser());
        }
        User savedUser = userRepository.save(oldUser);
        UserModel savedUserModel = userMapper.toModel(savedUser);
        Long countSubscribers = userRepository.getSubscriptionsCountByIdUser(savedUserModel.getIdUser());
        savedUserModel.setCountSubscribers(countSubscribers);
        logger.info("User updated successfully with id: {}", savedUser.getIdUser());
        return savedUserModel;
    }

    @CachePut(value = "users", key = "#result.idUser")
    public UserModel addSubscription(UserModel userModel) {
        logger.info("Adding subscription for user with id: {}", userModel.getIdUser());
        User oldUser = userRepository.findById(userModel.getIdUser())
                .orElseThrow(() -> {
                    logger.error("User with id {} not found", userModel.getIdUser());
                    return new NotFoundException("User not found");
                });
        List<User> subscriptions = userMapper.toEntityList(userModel.getSubscriptions());
        oldUser.getSubscriptions().addAll(subscriptions);
        User savedUser = userRepository.save(oldUser);
        UserModel savedUserModel = userMapper.toModel(savedUser);
        Long countSubscribers = userRepository.getSubscriptionsCountByIdUser(savedUserModel.getIdUser());
        savedUserModel.setCountSubscribers(countSubscribers);
        logger.info("Subscription with id: {} added successfully for user with id: {}",
                userModel.getSubscriptions().get(0).getIdUser(), savedUser.getIdUser());
        return savedUserModel;
    }

    @CachePut(value = "users", key = "#result.idUser")
    public UserModel deleteSubscription(UserModel userModel) {
        logger.info("Deleting subscription for user with id: {}", userModel.getIdUser());
        User oldUser = userRepository.findById(userModel.getIdUser())
                .orElseThrow(() -> {
                    logger.error("User with id: {} not found", userModel.getIdUser());
                    return new NotFoundException("User not found");
                });
        Long idSubscription = userModel.getSubscriptions().get(0).getIdUser();
        oldUser.getSubscriptions().removeIf(user -> user.getIdUser().equals(idSubscription));
        User savedUser = userRepository.save(oldUser);
        UserModel savedUserModel = userMapper.toModel(savedUser);
        Long countSubscribers = userRepository.getSubscriptionsCountByIdUser(savedUserModel.getIdUser());
        savedUserModel.setCountSubscribers(countSubscribers);
        logger.info("Subscription with id: {} deleted successfully for user with id: {}",
                userModel.getSubscriptions().get(0).getIdUser(), savedUser.getIdUser());
        return savedUserModel;
    }

    @CacheEvict(value = "users", key = "#id")
    public void delete(Long id) {
        logger.info("Deleting user with id: {}", id);
        String iconPath = "imageIconUser\\" + id + ".jpeg";
        String headerPath = "imageHeaderUser\\" + id + ".jpeg";
        userRepository.deleteById(id);
        logger.info("User deleted with id: {}", id);
        try {
            mediaService.deleteMedia(iconPath);
            logger.info("Associated media deleted");
        } catch (DeleteFileException ex) {
            logger.warn("Failed to delete associated media");
        }
        try {
            mediaService.deleteMedia(headerPath);
            logger.info("Associated media deleted");
        } catch (DeleteFileException ex) {
            logger.warn("Failed to delete associated media");
        }
    }

    @Cacheable(value = "users", key = "#id")
    public UserModel findUserById(Long id) {
        logger.info("Finding user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User with id {} not found", id);
                    return new NotFoundException("User not found");
                });
        UserModel userModel = userMapper.toModel(user);
        Long countSubscribers = userRepository.getSubscriptionsCountByIdUser(id);
        userModel.setCountSubscribers(countSubscribers);
        logger.info("Found user with id: {}", id);
        return userModel;
    }
}

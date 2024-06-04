package com.example.videohosting.controller;

import com.example.videohosting.dto.jwtResponse.JwtResponse;
import com.example.videohosting.dto.userDto.CreateUserRequest;
import com.example.videohosting.dto.userDto.UserLogInRequest;
import com.example.videohosting.config.jwtConfig.JwtUtils;
import com.example.videohosting.mapper.userMapper.UserMapper;
import com.example.videohosting.model.UserModel;
import com.example.videohosting.service.MediaService;
import com.example.videohosting.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class JwtAuthorizationController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public JwtAuthorizationController(UserService userService, UserMapper userMapper,
                                      AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                                      MediaService mediaService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signUp")
    public ResponseEntity<JwtResponse> signUp(@Valid @RequestBody CreateUserRequest request) {
        UserModel model = userMapper.toModelFromCreateRequest(request);
        UserModel userModel = userService.insert(model, request.getImageHeader(), request.getImageIcon());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        JwtResponse response = new JwtResponse(jwt, userModel.getEmail(), userModel.getIdUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/logIn")
    public ResponseEntity<JwtResponse> logIn(@Valid @RequestBody UserLogInRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        UserModel userModel = (UserModel) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);
        JwtResponse response = new JwtResponse(jwt, userModel.getEmail(), userModel.getIdUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

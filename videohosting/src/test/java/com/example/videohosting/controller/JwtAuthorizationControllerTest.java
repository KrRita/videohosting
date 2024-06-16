package com.example.videohosting.controller;

import com.example.videohosting.dto.jwtResponse.JwtResponse;
import com.example.videohosting.dto.userDto.CreateUserRequest;
import com.example.videohosting.dto.userDto.UserLogInRequest;
import com.example.videohosting.exception.UserAlreadyExistsException;
import com.example.videohosting.utils.UserUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
class JwtAuthorizationControllerTest {

    @Container
    public static PostgreSQLContainer<?> postgresqlContainer =
            new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @Autowired
    private JwtAuthorizationController jwtAuthorizationController;
    @Autowired
    private UserUtils userUtils;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    public void tearDown() {
        userUtils.tearDown();
    }

    @Test
    void signUp() throws JsonProcessingException {
        String email = "email@mail.ru";
        CreateUserRequest request = new CreateUserRequest(email, "test", "test", "test");
        String stringRequest = objectMapper.writeValueAsString(request);
        JwtResponse response = jwtAuthorizationController.signUp(stringRequest,null,null).getBody();
        assert response != null;
        assertEquals(email, response.getEmail());
    }

    @Test
    void signUpNegativeTest() throws JsonProcessingException {
        String email = "email@mail.ru";
        CreateUserRequest request = new CreateUserRequest(email, "test", "test", "test");
        String stringRequest = objectMapper.writeValueAsString(request);
        jwtAuthorizationController.signUp(stringRequest,null,null);
        CreateUserRequest failRequest = new CreateUserRequest(email, "CAT", "CAT", "CAT");
        String newStringRequest = objectMapper.writeValueAsString(failRequest);
        assertThrows(UserAlreadyExistsException.class, () -> jwtAuthorizationController.signUp(newStringRequest,null,null));
    }


    @Test
    void logIn() throws JsonProcessingException {
        String email = "email@mail.ru";
        String password ="password";
        CreateUserRequest createRequest = new CreateUserRequest(email, "test", "test", password);
        String stringRequest = objectMapper.writeValueAsString(createRequest);
        jwtAuthorizationController.signUp(stringRequest,null,null);
        UserLogInRequest request = new UserLogInRequest(email, password);
        JwtResponse response = jwtAuthorizationController.logIn(request).getBody();
        assert response != null;
        assertEquals(email, response.getEmail());
    }

    @Test
    void logInNegativeTest() {
        String email = "email";
        String password = "password";
        UserLogInRequest request = new UserLogInRequest(email, password);
        assertThrows(BadCredentialsException.class, () -> jwtAuthorizationController.logIn(request));
    }

}
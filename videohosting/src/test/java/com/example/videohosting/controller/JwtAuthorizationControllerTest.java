package com.example.videohosting.controller;

import com.example.videohosting.dto.jwtResponse.JwtResponse;
import com.example.videohosting.dto.userDto.CreateUserRequest;
import com.example.videohosting.dto.userDto.UserLogInRequest;
import com.example.videohosting.exception.UserAlreadyExistsException;
import com.example.videohosting.utils.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
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

    @AfterEach
    public void tearDown() {
        userUtils.tearDown();
    }

    @Test
    void signUp() {
        String email = "email@mail.ru";
        CreateUserRequest request = new CreateUserRequest(email, "test", null,
                null, "test", "test");
        ResponseEntity<JwtResponse> response = jwtAuthorizationController.signUp(request);
        assertEquals(email, response.getBody().getEmail());
    }

    @Test
    void signUpNegativeTest() {
        String email = "email@mail.ru";
        CreateUserRequest request = new CreateUserRequest(email, "test", null,
                null, "test", "test");
        jwtAuthorizationController.signUp(request);
        CreateUserRequest failRequest = new CreateUserRequest(email, "CAT", null,
                null, "CAT", "CAT");
        assertThrows(UserAlreadyExistsException.class, () -> jwtAuthorizationController.signUp(failRequest));
    }


    @Test
    void logIn() {
        String email = "email@mail.ru";
        String password ="password";
        CreateUserRequest createRequest = new CreateUserRequest(email, "test", null,
                null, "test", password);
        jwtAuthorizationController.signUp(createRequest);
        UserLogInRequest request = new UserLogInRequest(email, password);
        ResponseEntity<JwtResponse> response = jwtAuthorizationController.logIn(request);
        assertEquals(email, response.getBody().getEmail());
    }

    @Test
    void logInNegativeTest() {
        String email = "email";
        String password = "password";
        UserLogInRequest request = new UserLogInRequest(email, password);
        assertThrows(BadCredentialsException.class, () -> jwtAuthorizationController.logIn(request));
    }

}
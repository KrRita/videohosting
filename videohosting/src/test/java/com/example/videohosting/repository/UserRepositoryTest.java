package com.example.videohosting.repository;

import com.example.videohosting.entity.User;
import com.example.videohosting.utils.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Testcontainers
class UserRepositoryTest {
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
    private UserRepository userRepository;
    @Autowired
    private UserUtils utils;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void getSubscriptionsCountByIdUser() {
        User user = utils.createAndSaveUser();
        Long count = userRepository.getSubscriptionsCountByIdUser(user.getIdUser());
        assertEquals(1L, count);

    }

    @Transactional
    @Test
    void getUserByEmail() {
        User user = utils.createAndSaveUser();
        String email = user.getEmail();
        User result = userRepository.getUserByEmail(email);
        assertEquals(user, result);
    }
    @Test
    void getUserByEmailNegativeTest() {
        User result = userRepository.getUserByEmail("cat@example.ru");
        assertNull(result);
    }

}
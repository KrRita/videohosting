package com.example.videohosting.repository;

import com.example.videohosting.entity.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class CategoryRepositoryTest {

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
    private CategoryRepository repository;

    private Category createAndSaveCategory() {
        String name = "Cats";
        Category category = new Category();
        category.setName(name);
        return repository.save(category);
    }

    @Test
    void getCategoryByName() {
        Category category = createAndSaveCategory();
        Category result = repository.getCategoryByName(category.getName());
        assertEquals(category, result);
    }

    @Test
    void getCategoryByNameNegativeTest() {
        createAndSaveCategory();
        Category result = repository.getCategoryByName("Sport");
        assertNull(result);
    }

}
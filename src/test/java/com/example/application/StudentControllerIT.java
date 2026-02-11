package com.example.application;

import com.example.application.dto.StudentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Integration tests for StudentController with Testcontainers")
class StudentControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    @DisplayName("POST /api/students should create student in real DB")
    void createStudent_integrationTest() {
        StudentRequest request = new StudentRequest();
        request.setFirstName("Misha");
        request.setLastName("Ivanov");
        request.setStudyGroup("A-101");
        request.setEmail("misha.kremloy@gmail.com");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/students",
                request,
                String.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().contains("Misha"));
    }

    @Test
    @DisplayName("GET /api/students/{id} should return saved student")
    void getStudentById_integrationTest() {
        StudentRequest request = new StudentRequest();
        request.setFirstName("Anna");
        request.setLastName("Petrova");
        request.setStudyGroup("B-202");
        request.setEmail("misha.kremloy@gmail.com");

        ResponseEntity<String> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/students",
                request,
                String.class
        );

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        ResponseEntity<String> getResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/students/1",
                String.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertTrue(getResponse.getBody().contains("Anna"));
    }
}


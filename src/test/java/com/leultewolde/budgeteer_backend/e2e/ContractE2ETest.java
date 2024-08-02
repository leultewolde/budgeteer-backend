package com.leultewolde.budgeteer_backend.e2e;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("e2e_test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractE2ETest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @LocalServerPort
    private int port;

    @BeforeAll
    static void startContainer() {
        postgres.start();
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgres.getUsername());
        System.setProperty("DB_PASSWORD", postgres.getPassword());
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    private static final String CONTRACT_JSON = "{ \"lengthInMonths\": 12, \"salaryPerHour\": 50.00, \"maxHours\": 40, \"status\": \"ACTIVE\" }";

    @Test
    @Order(1)
    void addContract_ShouldCreateContract() {
        given()
                .contentType("application/json")
                .body(CONTRACT_JSON)
                .when()
                .post("/contracts")
                .then()
                .statusCode(201)
                .body("contractId", notNullValue())
                .body("lengthInMonths", equalTo(12))
                .body("salaryPerHour", equalTo(50.00f))
                .body("maxHours", equalTo(40))
                .body("status", equalTo("ACTIVE"));
    }

    @Test
    @Order(2)
    void getContracts_ShouldReturnAllContracts() {
        given()
                .contentType("application/json")
                .when()
                .get("/contracts")
                .then()
                .statusCode(200)
                .body("[0].contractId", notNullValue());
    }

    @Test
    @Order(3)
    void updateContract_ShouldUpdateExistingContract() {
        String updateJson = "{ \"lengthInMonths\": 24, \"salaryPerHour\": 60.00, \"maxHours\": 35, \"status\": \"INACTIVE\" }";

        given()
                .contentType("application/json")
                .body(updateJson)
                .when()
                .put("/contracts/1")
                .then()
                .statusCode(200)
                .body("lengthInMonths", equalTo(24))
                .body("salaryPerHour", equalTo(60.00f))
                .body("maxHours", equalTo(35))
                .body("status", equalTo("INACTIVE"));
    }

    @Test
    @Order(4)
    void deleteContract_ShouldDeleteContract() {
        given()
                .contentType("application/json")
                .when()
                .delete("/contracts/1")
                .then()
                .statusCode(204);
    }
}

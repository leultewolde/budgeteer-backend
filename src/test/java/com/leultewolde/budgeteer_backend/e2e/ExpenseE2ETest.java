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
public class ExpenseE2ETest {

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

    @Test
    @Order(1)
    void addExpense_ShouldCreateExpense() {
        String expenseJson = "{ \"date\": \"2024-08-01\", \"category\": \"NEEDS\", \"amount\": 100.00 }";

        given()
                .contentType("application/json")
                .body(expenseJson)
                .when()
                .post("/expenses")
                .then()
                .statusCode(201)
                .body("expenseId", notNullValue())
                .body("date", equalTo("2024-08-01"))
                .body("category", equalTo("NEEDS"))
                .body("amount", equalTo(100.00f));
    }

    @Test
    @Order(2)
    void getExpenses_ShouldReturnAllExpenses() {
        given()
                .contentType("application/json")
                .when()
                .get("/expenses")
                .then()
                .statusCode(200)
                .body("[0].expenseId", notNullValue());
    }

    @Test
    @Order(3)
    void updateExpense_ShouldUpdateExpense() {
        String updateJson = "{ \"date\": \"2024-08-02\", \"category\": \"WANTS\", \"amount\": 50.00 }";

        // Log the initial state before the update
        System.out.println("Updating expense with JSON: " + updateJson);

        given()
                .contentType("application/json")
                .body(updateJson)
                .when()
                .put("/expenses/1")
                .then()
                .statusCode(200)
                .body("date", equalTo("2024-08-02"))
                .body("category", equalTo("WANTS"))
                .body("amount", equalTo(50.00f));
    }

    @Test
    @Order(4)
    void deleteExpense_ShouldDeleteExpense() {
        given()
                .contentType("application/json")
                .when()
                .delete("/expenses/1")
                .then()
                .statusCode(204);
    }
}

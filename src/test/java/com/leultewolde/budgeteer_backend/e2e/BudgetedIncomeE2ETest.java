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
public class BudgetedIncomeE2ETest {

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
    void addBudgetedIncome_ShouldCreateIncome() {
        String contractJson = "{ \"lengthInMonths\": 12, \"salaryPerHour\": 50.00, \"maxHours\": 40, \"status\": \"ACTIVE\" }";
        given()
                .contentType("application/json")
                .body(contractJson)
                .when()
                .post("/contracts")
                .then()
                .statusCode(201)
                .body("contractId", notNullValue());

        String incomeJson = "{ \"contractId\": 1, \"hoursWorked\": 40, \"date\": \"2024-08-01\", \"payPeriodStart\": \"2024-07-25\", \"payPeriodEnd\": \"2024-08-01\" }";

        given()
                .contentType("application/json")
                .body(incomeJson)
                .when()
                .post("/budgeted-incomes")
                .then()
                .statusCode(201)
                .body("incomeId", notNullValue())
                .body("hoursWorked", equalTo(40))
                .body("date", equalTo("2024-08-01"))
                .body("payPeriodStart", equalTo("2024-07-25"))
                .body("payPeriodEnd", equalTo("2024-08-01"));
    }

    @Test
    @Order(2)
    void getBudgetedIncomes_ShouldReturnAllIncomes() {
        given()
                .contentType("application/json")
                .when()
                .get("/budgeted-incomes")
                .then()
                .statusCode(200)
                .body("[0].incomeId", notNullValue());
    }

    @Test
    @Order(3)
    void updateBudgetedIncome_ShouldUpdateIncome() {
        String updateJson = "{ \"contractId\": 1, \"hoursWorked\": 35, \"date\": \"2024-08-01\", \"payPeriodStart\": \"2024-07-18\", \"payPeriodEnd\": \"2024-07-25\" }";

        given()
                .contentType("application/json")
                .body(updateJson)
                .when()
                .put("/budgeted-incomes/1")
                .then()
                .statusCode(200)
                .body("hoursWorked", equalTo(35))
                .body("date", equalTo("2024-08-01"))
                .body("payPeriodStart", equalTo("2024-07-18"))
                .body("payPeriodEnd", equalTo("2024-07-25"));
    }

    @Test
    @Order(4)
    void deleteBudgetedIncome_ShouldDeleteIncome() {
        given()
                .contentType("application/json")
                .when()
                .delete("/budgeted-incomes/1")
                .then()
                .statusCode(204);
    }
}

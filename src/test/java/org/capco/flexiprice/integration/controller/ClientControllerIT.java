package org.capco.flexiprice.integration.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.capco.flexiprice.integration.DataBaseConfiguration;
import org.capco.flexiprice.repository.client.ProfessionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(DataBaseConfiguration.class)
public class ClientControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        professionalRepository.deleteAll();
    }

    @Test
    void should_create_client_and_return_201_with_person_response_DTO() {
        String requestBody = """
                {
                    "legalName": "SCI Stock Market",
                    "vatNumber": "FR12345678901",
                    "sirenNumber": "732829320",
                    "annualRevenue": 10000000
                }""";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/api/v1/clients/professional")
        .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        "id", notNullValue(),
                        "legalName", equalTo("SCI Stock Market"),
                        "vatNumber", equalTo("FR12345678901"),
                        "sirenNumber", equalTo("732829320"),
                        "annualRevenue", equalTo(10000000)
                );
    }

}

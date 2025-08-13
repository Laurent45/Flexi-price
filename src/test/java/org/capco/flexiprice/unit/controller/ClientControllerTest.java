package org.capco.flexiprice.unit.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.capco.flexiprice.controller.client.ClientController;
import org.capco.flexiprice.dto.ApiErrorDTO;
import org.capco.flexiprice.dto.PersonalClientResponseDTO;
import org.capco.flexiprice.dto.ProfessionalResponseDTO;
import org.capco.flexiprice.enumeration.ClientType;
import org.capco.flexiprice.exception.ProfessionalNotFoundException;
import org.capco.flexiprice.exception.UserNameAlreadyExistsException;
import org.capco.flexiprice.service.client.PersonalService;
import org.capco.flexiprice.service.client.ProfessionalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ClientController.class)
public class ClientControllerTest {

    @MockitoBean
    private ProfessionalService professionalService;

    @MockitoBean
    private PersonalService personalService;

    @Autowired
    private MockMvc mockMvc;


    @Nested
    class ProfessionalClientClientTest {

        @BeforeEach
        void setUp() {
            RestAssuredMockMvc.mockMvc(mockMvc);
        }

        @Test
        void should_create_professional_client() {
            // GIVEN
            String requestBody = """
                    {
                        "legalName": "SCI Stock Market",
                        "vatNumber": "FR12345678901",
                        "sirenNumber": "732829320",
                        "annualRevenue": 10000000
                    }""";

            ProfessionalResponseDTO responseBodyExpected = new ProfessionalResponseDTO(
                    1L,
                    "SCI Stock Market",
                    "FR12345678901",
                    "732829320",
                    BigDecimal.valueOf(10_000_000),
                    ClientType.PROFESSIONAL_REVENUE_LT_10M,
                    1L
            );

            when(professionalService.saveProfessional(any())).thenReturn(responseBodyExpected);

            // WHEN
            ProfessionalResponseDTO response = given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .post("/api/v1/clients/professional")
                    .then()
                    // THEN
                    .statusCode(201)
                    .contentType(ContentType.JSON)
                    .extract()
                    .body()
                    .as(ProfessionalResponseDTO.class);

            // THEN
            assertThat(response).isEqualTo(responseBodyExpected);
        }

        @Test
        void should_return_bad_request_when_siren_number_already_exists() {
            // GIVEN
            String requestBody = """
                    {
                        "legalName": "SCI Stock Market",
                        "vatNumber": "FR12345678901",
                        "sirenNumber": "732829320",
                        "annualRevenue": 10000000
                    }""";

            String errorMessage = "SIREN number '732829320' already exists";
            when(professionalService.saveProfessional(any())).thenThrow(new UserNameAlreadyExistsException(errorMessage));

            // WHEN
            ApiErrorDTO response = given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .post("/api/v1/clients/professional")
                    .then()
                    // THEN
                    .statusCode(400)
                    .contentType(ContentType.JSON)
                    .extract()
                    .body()
                    .as(ApiErrorDTO.class);

            // THEN
            assertThat(response).isEqualTo(new ApiErrorDTO(HttpStatus.BAD_REQUEST.value(), "Bad Request", errorMessage));
        }

        @ParameterizedTest
        @MethodSource("invalidProfessionalClientDTOs")
        void should_return_bad_request_when_request_dto_is_not_valid(RequestBodyAndApiError input) {
            // WHEN
            ApiErrorDTO response = given()
                    .contentType(ContentType.JSON)
                    .body(input.requestBody)
                    .post("/api/v1/clients/professional")
                    .then()
                    // THEN
                    .statusCode(400)
                    .contentType(ContentType.JSON)
                    .extract()
                    .body()
                    .as(ApiErrorDTO.class);
            // THEN
            assertThat(response).isEqualTo(input.apiErrorDTO);
        }

        @Test
        void should_return_professional_client_by_siren_number() {
            // GIVEN
            String sirenNumber = "732829320";
            ProfessionalResponseDTO responseBodyExpected = new ProfessionalResponseDTO(
                    1L,
                    "SCI Stock Market",
                    "FR12345678901",
                    sirenNumber,
                    BigDecimal.valueOf(10_000_000),
                    ClientType.PROFESSIONAL_REVENUE_LT_10M,
                    1L
            );

            when(professionalService.getProfessional(sirenNumber)).thenReturn(responseBodyExpected);

            // WHEN
            ProfessionalResponseDTO response = given()
                    .contentType(ContentType.JSON)
                    .get("/api/v1/clients/professional/{sirenNumber}", sirenNumber)
                    .then()
                    // THEN
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .extract()
                    .body()
                    .as(ProfessionalResponseDTO.class);

            // THEN
            assertThat(response).isEqualTo(responseBodyExpected);
        }

        @Test
        void should_return_not_found_when_professional_client_does_not_exist() {
            // GIVEN
            String sirenNumber = "999999999";
            String errorMessage = "Professional client with SIREN number '999999999' not found";
            when(professionalService.getProfessional(sirenNumber)).thenThrow(new ProfessionalNotFoundException(errorMessage));

            // WHEN
            ApiErrorDTO response = given()
                    .contentType(ContentType.JSON)
                    .get("/api/v1/clients/professional/{sirenNumber}", sirenNumber)
                    .then()
                    // THEN
                    .statusCode(404)
                    .contentType(ContentType.JSON)
                    .extract()
                    .body()
                    .as(ApiErrorDTO.class);

            // THEN
            assertThat(response).isEqualTo(new ApiErrorDTO(HttpStatus.NOT_FOUND.value(), "Not Found", errorMessage));
        }

        private static Stream<RequestBodyAndApiError> invalidProfessionalClientDTOs() {
            return Stream.of(
                    new RequestBodyAndApiError(
                            """
                                    {
                                        "legalName": "",
                                        "vatNumber": "FR12345678901",
                                        "sirenNumber": "732829320",
                                        "annualRevenue": 10000000
                                    }""",
                            new ApiErrorDTO(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Validation error: [Legal name must not be blank]")
                    ),
                    new RequestBodyAndApiError(
                            """
                                    {
                                        "legalName": "SCI Stock Market",
                                        "vatNumber": "732829320",
                                        "sirenNumber": "732829320"
                                    }""",
                            new ApiErrorDTO(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Validation error: [Annual revenue must not be null]")
                    ),
                    new RequestBodyAndApiError(
                            """
                                    {
                                        "legalName": "SCI Stock Market",
                                        "vatNumber": "FR12345678901",
                                        "sirenNumber": "",
                                        "annualRevenue": 10000000
                                    }""",
                            new ApiErrorDTO(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Validation error: [SIREN number must not be blank]")
                    )
            );
        }
    }

    @Nested
    class PersonalClientTest {

        @BeforeEach
        void setUp() {
            RestAssuredMockMvc.mockMvc(mockMvc);
        }

        @Test
        void should_create_personal_client() {
            // GIVEN
            String requestBody = """
                    {
                        "lastName": "Doe",
                        "firstName": "John",
                        "username": "john_doe"
                    }""";

            PersonalClientResponseDTO responseBodyExpected = new PersonalClientResponseDTO(
                    1L,
                    "Doe",
                    "John",
                    "john_doe",
                    1L
            );

            when(personalService.savePersonalClient(any())).thenReturn(responseBodyExpected);

            // WHEN
            PersonalClientResponseDTO response = given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .post("/api/v1/clients/personal")
                    .then()
                    // THEN
                    .statusCode(201)
                    .contentType(ContentType.JSON)
                    .extract()
                    .body()
                    .as(PersonalClientResponseDTO.class);

            // THEN
            assertThat(response).isEqualTo(responseBodyExpected);
        }

        @Test
        void should_return_bad_request_when_username_already_exists() {
            // GIVEN
            String requestBody = """
                    {
                        "lastName": "Doe",
                        "firstName": "John",
                        "username": "john_doe"
                    }""";

            String errorMessage = "Username 'john_doe' already exists";
            when(personalService.savePersonalClient(any())).thenThrow(new UserNameAlreadyExistsException(errorMessage));

            // WHEN
            ApiErrorDTO response = given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .post("/api/v1/clients/personal")
                    .then()
                    // THEN
                    .statusCode(400)
                    .contentType(ContentType.JSON)
                    .extract()
                    .body()
                    .as(ApiErrorDTO.class);

            // THEN
            assertThat(response).isEqualTo(new ApiErrorDTO(HttpStatus.BAD_REQUEST.value(), "Bad Request", errorMessage));
        }

        @ParameterizedTest
        @MethodSource("invalidPersonalClientDTOs")
        void should_return_bad_request_when_request_dto_is_not_valid(RequestBodyAndApiError input) {
            // WHEN
            ApiErrorDTO response = given()
                    .contentType(ContentType.JSON)
                    .body(input.requestBody)
                    .post("/api/v1/clients/personal")
                    .then()
                    // THEN
                    .statusCode(400)
                    .contentType(ContentType.JSON)
                    .extract()
                    .body()
                    .as(ApiErrorDTO.class);
            // THEN
            assertThat(response).isEqualTo(input.apiErrorDTO);
        }

        @Test
        void should_return_personal_client_by_username() {
            // GIVEN
            String username = "john_doe";
            PersonalClientResponseDTO responseBodyExpected = new PersonalClientResponseDTO(
                    1L,
                    "Doe",
                    "John",
                    username,
                    1L
            );

            when(personalService.getPersonalClient(username)).thenReturn(responseBodyExpected);

            // WHEN
            PersonalClientResponseDTO response = given()
                    .contentType(ContentType.JSON)
                    .get("/api/v1/clients/personal/{username}", username)
                    .then()
                    // THEN
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .extract()
                    .body()
                    .as(PersonalClientResponseDTO.class);

            // THEN
            assertThat(response).isEqualTo(responseBodyExpected);
        }

        @Test
        void should_return_not_found_when_personal_client_does_not_exist() {
            // GIVEN
            String username = "unknown_user";
            String errorMessage = "Personal client with username 'unknown_user' not found";
            when(personalService.getPersonalClient(username)).thenThrow(new ProfessionalNotFoundException(errorMessage));

            // WHEN
            ApiErrorDTO response = given()
                    .contentType(ContentType.JSON)
                    .get("/api/v1/clients/personal/{username}", username)
                    .then()
                    // THEN
                    .statusCode(404)
                    .contentType(ContentType.JSON)
                    .extract()
                    .body()
                    .as(ApiErrorDTO.class);

            // THEN
            assertThat(response).isEqualTo(new ApiErrorDTO(HttpStatus.NOT_FOUND.value(), "Not Found", errorMessage));
        }

        private static Stream<RequestBodyAndApiError> invalidPersonalClientDTOs() {
            return Stream.of(
                    new RequestBodyAndApiError(
                            """
                                    {
                                        "lastName": "  ",
                                        "firstName": "John",
                                        "username": "john_doe"
                                    }""",
                            new ApiErrorDTO(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Validation error: [Last name must not be blank]")
                    ),
                    new RequestBodyAndApiError(
                            """
                                    {
                                        "lastName": "Doe",
                                        "firstName": "",
                                        "username": "john_doe"
                                    }""",
                            new ApiErrorDTO(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Validation error: [First name must not be blank]")
                    ),
                    new RequestBodyAndApiError(
                            """
                                    {
                                        "lastName": "Doe",
                                        "firstName": "John"
                                    }""",
                            new ApiErrorDTO(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Validation error: [Username must not be blank]")
                    )
            );
        }

    }

    private record RequestBodyAndApiError(
            String requestBody,
            ApiErrorDTO apiErrorDTO
    ) {
    }
}

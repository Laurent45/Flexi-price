package org.capco.flexiprice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.capco.flexiprice.controller.client.ClientController;
import org.capco.flexiprice.dto.ProfessionalSaveRequestDTO;
import org.capco.flexiprice.service.client.PersonalService;
import org.capco.flexiprice.service.client.ProfessionalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClientController.class)
public class ClientControllerTest {

    @MockitoBean
    private ProfessionalService professionalService;

    @MockitoBean
    private PersonalService personalService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_create_professional_client() throws Exception {
        // GIVEN
        ProfessionalSaveRequestDTO request = new ProfessionalSaveRequestDTO(
                "SCI Stock Market",
                "FR12345678901",
                "732829320",
                BigDecimal.valueOf(10_000_000)
        );

        // WHEN
        mockMvc.perform(post("/api/v1/clients/professional")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // THEN
                .andExpect(status().isCreated());

    }

    @ParameterizedTest
    @MethodSource("invalidProfessionalDTOs")
    void should_return_bad_request_when_request_dto_is_not_valid(ProfessionalSaveRequestDTO request) throws Exception {
        // WHEN
        mockMvc.perform(post("/api/v1/clients/professional")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // THEN
                .andExpect(status().isBadRequest());
    }

    private static Stream<ProfessionalSaveRequestDTO> invalidProfessionalDTOs() {
        return Stream.of(
                new ProfessionalSaveRequestDTO(null, "FR12345678901", "732829320", BigDecimal.valueOf(10_000_000)),
                new ProfessionalSaveRequestDTO("   ", "FR12345678901", "732829320", BigDecimal.valueOf(10_000_000)),
                new ProfessionalSaveRequestDTO("", "FR12345678901", "732829320", BigDecimal.valueOf(10_000_000)),
                new ProfessionalSaveRequestDTO("SCI Stock Market", "FR12345678901", null, BigDecimal.valueOf(10_000_000)),
                new ProfessionalSaveRequestDTO("SCI Stock Market", "FR12345678901", "      ", BigDecimal.valueOf(10_000_000)),
                new ProfessionalSaveRequestDTO("SCI Stock Market", "FR12345678901", "", BigDecimal.valueOf(10_000_000)),
                new ProfessionalSaveRequestDTO("SCI Stock Market", "FR12345678901", "732829320", null)
        );
    }
}

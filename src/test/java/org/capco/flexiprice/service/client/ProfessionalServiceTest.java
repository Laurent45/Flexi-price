package org.capco.flexiprice.service.client;

import org.capco.flexiprice.dto.ProfessionalResponseDTO;
import org.capco.flexiprice.dto.ProfessionalSaveRequestDTO;
import org.capco.flexiprice.entity.client.ProfessionalClient;
import org.capco.flexiprice.enumeration.ClientType;
import org.capco.flexiprice.exception.ProfessionalNotFoundException;
import org.capco.flexiprice.exception.SirenNumberAlreadyExistsException;
import org.capco.flexiprice.repository.client.ProfessionalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfessionalServiceTest {

    @Mock
    private ProfessionalRepository professionalRepository;

    @InjectMocks
    private ProfessionalService professionalService;

    @Test
    void should_save_professional_client() {
        // GIVEN
        String legalName = "SCI Stock Market";
        String vatNumber = "FR12345678901";
        String sirenNumber = "732829320";
        BigDecimal annualRevenue = BigDecimal.valueOf(10_000_000);
        ClientType clientType = ClientType.PROFESSIONAL_REVENUE_GTE_10M;
        Long clientId = 1L;
        Long cartId = 1L;

        ProfessionalSaveRequestDTO request = new ProfessionalSaveRequestDTO(
                legalName, vatNumber, sirenNumber, annualRevenue
        );

        ProfessionalClient professional = ProfessionalClient.create(
                clientId, legalName, vatNumber, sirenNumber, annualRevenue, cartId
        );

        ProfessionalResponseDTO expected = new ProfessionalResponseDTO(
                clientId, legalName, vatNumber, sirenNumber, annualRevenue, clientType, cartId
        );

        when(professionalRepository.existsProfessionalBySirenNumber(sirenNumber)).thenReturn(false);
        when(professionalRepository.save(any())).thenReturn(professional);

        // WHEN
        ProfessionalResponseDTO response = professionalService.saveProfessional(request);

        // THEN
        assertThat(response).isEqualTo(expected);
    }

    @Test
    void throw_exception_when_siren_number_already_exists() {
        // GIVEN
        String legalName = "SCI Stock Market";
        String vatNumber = "FR12345678901";
        String sirenNumber = "732829320";
        BigDecimal annualRevenue = BigDecimal.valueOf(10_000_000);

        ProfessionalSaveRequestDTO request = new ProfessionalSaveRequestDTO(
                legalName, vatNumber, sirenNumber, annualRevenue
        );

        when(professionalRepository.existsProfessionalBySirenNumber(sirenNumber)).thenReturn(true);

        // WHEN
        assertThatThrownBy(() -> professionalService.saveProfessional(request))
                // THEN
                .isInstanceOf(SirenNumberAlreadyExistsException.class);
    }

    @Test
    void should_retrieve_professional_by_siren_number() {
        // GIVEN
        String sirenNumber = "732829320";
        ProfessionalClient professional = ProfessionalClient.create(1L,
                "SCI Stock Market",
                "FR12345678901",
                sirenNumber,
                BigDecimal.valueOf(10_000_000),
                1L);
        ProfessionalResponseDTO expectedResponse = new ProfessionalResponseDTO(
                1L,
                "SCI Stock Market",
                "FR12345678901",
                sirenNumber,
                BigDecimal.valueOf(10_000_000),
                ClientType.PROFESSIONAL_REVENUE_GTE_10M,
                1L
        );

        when(professionalRepository.findProfessionalBySirenNumber(sirenNumber)).thenReturn(Optional.of(professional));

        // WHEN
        ProfessionalResponseDTO response = professionalService.getProfessional(sirenNumber);

        // THEN
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void should_throw_exception_when_professional_not_found() {
        // GIVEN
        String sirenNumber = "732829320";

        when(professionalRepository.findProfessionalBySirenNumber(sirenNumber)).thenReturn(Optional.empty());

        // WHEN
        assertThatThrownBy(() -> professionalService.getProfessional(sirenNumber))
                // THEN
                .isInstanceOf(ProfessionalNotFoundException.class)
                .hasMessageContaining("Professional not found for sirenNumber: " + sirenNumber);
    }
}
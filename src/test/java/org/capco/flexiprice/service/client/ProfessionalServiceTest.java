package org.capco.flexiprice.service.client;

import org.capco.flexiprice.dto.ProfessionalResponseDTO;
import org.capco.flexiprice.dto.ProfessionalSaveRequestDTO;
import org.capco.flexiprice.entity.client.Professional;
import org.capco.flexiprice.enumeration.ClientType;
import org.capco.flexiprice.exception.SirenNumberAlreadyExistsException;
import org.capco.flexiprice.repository.client.ProfessionalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfessionalServiceTest {

    @Mock
    private ProfessionalRepository professionalRepository;

    @InjectMocks
    private ProfessionalService professionalService;

    @Test
    void save_professional_client() {
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

        Professional professional = Professional.create(
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
        verify(professionalRepository).existsProfessionalBySirenNumber(sirenNumber);
        verify(professionalRepository).save(any());
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

        // THEN
        verify(professionalRepository).existsProfessionalBySirenNumber(sirenNumber);
        verify(professionalRepository, never()).save(any());
    }
}
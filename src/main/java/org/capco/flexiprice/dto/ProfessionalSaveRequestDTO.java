package org.capco.flexiprice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.capco.flexiprice.entity.client.ProfessionalClient;

import java.math.BigDecimal;

public record ProfessionalSaveRequestDTO(
        @NotBlank(message = "Legal name must not be blank")
        String legalName,

        String vatNumber,

        @NotBlank(message = "SIREN number must not be blank")
        String sirenNumber,

        @NotNull(message = "Annual revenue must not be null")
        BigDecimal annualRevenue
) {
    public ProfessionalClient toProfessional() {
        return ProfessionalClient.create(legalName, vatNumber, sirenNumber, annualRevenue);
    }
}

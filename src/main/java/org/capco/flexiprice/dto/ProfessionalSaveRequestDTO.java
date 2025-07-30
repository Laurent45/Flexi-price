package org.capco.flexiprice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.capco.flexiprice.entity.client.Professional;

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
    public Professional toProfessional() {
        return Professional.create(legalName, vatNumber, sirenNumber, annualRevenue);
    }
}

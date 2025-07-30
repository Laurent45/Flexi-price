package org.capco.flexiprice.dto;

import org.capco.flexiprice.entity.client.Professional;
import org.capco.flexiprice.enumeration.ClientType;

import java.math.BigDecimal;

public record ProfessionalResponseDTO(
        Long id,
        String legalName,
        String vatNumber,
        String sirenNumber,
        BigDecimal annualRevenue,
        ClientType clientType,
        Long cartId
) {

    public static ProfessionalResponseDTO from(Professional professional) {
        return new ProfessionalResponseDTO(
                professional.getId(),
                professional.getLegalName(),
                professional.getVatNumber(),
                professional.getSirenNumber(),
                professional.getAnnualRevenue(),
                professional.getClientType(),
                professional.getCart().getId()
        );
    }
}

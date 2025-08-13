package org.capco.flexiprice.dto;

import org.capco.flexiprice.enumeration.ClientType;

import java.math.BigDecimal;

public record ProductPriceDTO(
        Long id,
        String productName,
        ClientType clientType,
        BigDecimal productPrice
) {
}

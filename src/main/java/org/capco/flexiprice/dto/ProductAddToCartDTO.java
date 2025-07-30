package org.capco.flexiprice.dto;

public record ProductAddToCartDTO(
        Long cartId,
        String productName,
        int quantity
) {
}

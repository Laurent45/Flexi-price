package org.capco.flexiprice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductAddRequestDTO(
        @NotNull(message = "Product name must not be null")
        String name,

        @Positive(message = "Quantity must be positive")
        @NotNull(message = "Quantity must not be null")
        Integer quantity
) {
}

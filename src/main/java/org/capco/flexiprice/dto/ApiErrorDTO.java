package org.capco.flexiprice.dto;

public record ApiErrorDTO(
        int status,
        String statusMessage,
        String message
) {
}

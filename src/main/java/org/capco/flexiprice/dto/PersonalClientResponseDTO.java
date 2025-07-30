package org.capco.flexiprice.dto;

import org.capco.flexiprice.entity.client.PersonalClient;

public record PersonalClientResponseDTO(
        Long id,
        String lastName,
        String firstName,
        String username,
        Long cartId
) {
    public static PersonalClientResponseDTO from(PersonalClient personalClient) {
        return new PersonalClientResponseDTO(
                personalClient.getId(),
                personalClient.getLastName(),
                personalClient.getFirstName(),
                personalClient.getUsername(),
                personalClient.getCart().getId());
    }
}

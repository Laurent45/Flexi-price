package org.capco.flexiprice.dto;

import jakarta.validation.constraints.NotBlank;
import org.capco.flexiprice.entity.client.PersonalClient;

public record PersonalSaveRequestDTO(
        @NotBlank(message = "Last name must not be blank")
        String lastName,

        @NotBlank(message = "First name must not be blank")
        String firstName,

        @NotBlank(message = "Username must not be blank")
        String username
) {
        public PersonalClient toPersonalClient() {
                return new PersonalClient(lastName, firstName, username);
        }
}

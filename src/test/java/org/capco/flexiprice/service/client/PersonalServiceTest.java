package org.capco.flexiprice.service.client;

import org.capco.flexiprice.dto.PersonalClientResponseDTO;
import org.capco.flexiprice.dto.PersonalSaveRequestDTO;
import org.capco.flexiprice.entity.client.PersonalClient;
import org.capco.flexiprice.exception.PersonalClientNotFoundException;
import org.capco.flexiprice.exception.UserNameAlreadyExistsException;
import org.capco.flexiprice.repository.client.PersonalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonalServiceTest {

    @Mock
    private PersonalRepository personalRepository;

    @InjectMocks
    private PersonalService personalService;

    @Test
    void should_save_personal_client() {
        // GIVEN
        PersonalSaveRequestDTO request = new PersonalSaveRequestDTO(
                "John",
                "Doe",
                "john_doe"
        );
        PersonalClient personalClient = new PersonalClient(
                1L,
                request.firstName(),
                request.lastName(),
                request.username(),
                1L
        );
        PersonalClientResponseDTO expectedResponse = new PersonalClientResponseDTO(
                personalClient.getId(),
                personalClient.getLastName(),
                personalClient.getFirstName(),
                personalClient.getUsername(),
                1L
        );

        when(personalRepository.existsPersonalByUsername(request.username())).thenReturn(false);
        when(personalRepository.save(any())).thenReturn(personalClient);

        //WHEN
        PersonalClientResponseDTO personalClientResponseDTO = personalService.savePersonalClient(request);

        // THEN
        assertThat(personalClientResponseDTO).isEqualTo(expectedResponse);
    }

    @Test
    void throw_exception_when_username_already_exists() {
        // GIVEN
        PersonalSaveRequestDTO request = new PersonalSaveRequestDTO(
                "Jane",
                "Doe",
                "jane_doe"
        );

        when(personalRepository.existsPersonalByUsername(request.username())).thenReturn(true);

        // WHEN
        assertThatThrownBy(() -> personalService.savePersonalClient(request))
                // THEN
                .isInstanceOf(UserNameAlreadyExistsException.class)
                .hasMessageContaining("Personal client already exists with username: " + request.username());
    }

    @Test
    void should_retrieve_personal_client_by_username() {
        // GIVEN
        String username = "john_doe";
        PersonalClient personalClient = new PersonalClient(
                1L,
                "John",
                "Doe",
                username,
                1L
        );
        PersonalClientResponseDTO expectedResponse = new PersonalClientResponseDTO(
                personalClient.getId(),
                personalClient.getLastName(),
                personalClient.getFirstName(),
                personalClient.getUsername(),
                1L
        );

        when(personalRepository.findByUsername(username)).thenReturn(Optional.of(personalClient));

        // WHEN
        PersonalClientResponseDTO response = personalService.getPersonalClient(username);

        // THEN
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void throw_exception_when_personal_client_not_found() {
        // GIVEN
        String username = "non_existent_user";

        when(personalRepository.findByUsername(username)).thenReturn(Optional.empty());

        // WHEN
        assertThatThrownBy(() -> personalService.getPersonalClient(username))
                // THEN
                .isInstanceOf(PersonalClientNotFoundException.class)
                .hasMessageContaining("Personal client with username " + username + " not found");
    }
}

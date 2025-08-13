package org.capco.flexiprice.service.client;

import org.capco.flexiprice.dto.PersonalClientResponseDTO;
import org.capco.flexiprice.dto.PersonalSaveRequestDTO;
import org.capco.flexiprice.entity.client.PersonalClient;
import org.capco.flexiprice.exception.PersonalClientNotFoundException;
import org.capco.flexiprice.exception.UserNameAlreadyExistsException;
import org.capco.flexiprice.repository.client.PersonalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonalService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonalService.class);

    private final PersonalRepository personalRepository;

    public PersonalService(PersonalRepository personalRepository) {
        this.personalRepository = personalRepository;
    }

    /**
     * Saves a new personal client.
     *
     * @param request the DTO containing personal client data to save
     * @return the response DTO representing the saved personal client
     * @throws UserNameAlreadyExistsException if a client with the given username already exists
     */
    @Transactional
    public PersonalClientResponseDTO savePersonalClient(PersonalSaveRequestDTO request) {
        LOG.info("Saving personal client: [{}]", request);

        if (personalRepository.existsPersonalByUsername(request.username())) {
            LOG.error("Personal client already exists with username: [{}]", request.username());
            throw new UserNameAlreadyExistsException("Personal client already exists with username: " + request.username());
        }

        PersonalClient personalClient = request.toPersonalClient();
        PersonalClient personalClientSaved = personalRepository.save(personalClient);

        LOG.info("Personal client saved with id : {}", personalClientSaved.getId());

        return PersonalClientResponseDTO.from(personalClientSaved);
    }

    /**
     * Retrieves a personal client by username.
     *
     * @param username the username of the personal client to retrieve
     * @return the response DTO representing the personal client
     * @throws PersonalClientNotFoundException if no client is found with the given username
     */
    @Transactional(readOnly = true)
    public PersonalClientResponseDTO getPersonalClient(String username) {
        LOG.info("Getting personal client with username : {}", username);
        PersonalClient personalClient = personalRepository.findByUsername(username)
                .orElseThrow(() -> {
                    LOG.error("Personal client with username {} not found", username);
                    return new PersonalClientNotFoundException("Personal client with username " + username + " not found");
                });

        return PersonalClientResponseDTO.from(personalClient);
    }
}

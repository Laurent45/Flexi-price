package org.capco.flexiprice.service.client;

import org.capco.flexiprice.dto.PersonalSaveRequestDTO;
import org.capco.flexiprice.entity.client.PersonalClient;
import org.capco.flexiprice.exception.PersonalClientNotFoundException;
import org.capco.flexiprice.exception.ProfessionalNotFoundException;
import org.capco.flexiprice.exception.UserNameAlreadyExistsException;
import org.capco.flexiprice.repository.client.PersonalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PersonalService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonalService.class);

    private final PersonalRepository personalRepository;

    public PersonalService(PersonalRepository personalRepository) {
        this.personalRepository = personalRepository;
    }

    public PersonalClient savePersonalClient(PersonalSaveRequestDTO request) {
        LOG.info("Saving personal client: [{}]", request);

        if (personalRepository.existsPersonalByUsername(request.username())) {
            LOG.error("Personal client already exists with username: [{}]", request.username());
            throw new UserNameAlreadyExistsException("Personal client already exists with username: " + request.username());
        }

        PersonalClient personalClient = request.toPersonalClient();
        PersonalClient personalClientSaved = personalRepository.save(personalClient);

        LOG.info("Personal client saved with id : {}", personalClient.getId());

        return personalClientSaved;
    }

    public PersonalClient getPersonalClient(String username) {
        LOG.info("Getting personal client with username : {}", username);
        return personalRepository.findByUsername(username)
                .orElseThrow(() -> {
                    LOG.error("Personal client with username {} not found", username);
                    return new PersonalClientNotFoundException("Personal client with username " + username + " not found");
                });
    }
}

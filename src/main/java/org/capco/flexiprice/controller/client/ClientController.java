package org.capco.flexiprice.controller.client;

import jakarta.validation.Valid;
import org.capco.flexiprice.dto.PersonalClientResponseDTO;
import org.capco.flexiprice.dto.PersonalSaveRequestDTO;
import org.capco.flexiprice.dto.ProfessionalResponseDTO;
import org.capco.flexiprice.dto.ProfessionalSaveRequestDTO;
import org.capco.flexiprice.service.client.PersonalService;
import org.capco.flexiprice.service.client.ProfessionalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);

    private final ProfessionalService professionalService;
    private final PersonalService personalService;

    public ClientController(ProfessionalService professionalService, PersonalService personalService) {
        this.professionalService = professionalService;
        this.personalService = personalService;
    }

    @PostMapping("/professional")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfessionalResponseDTO createProfessionalClient(@Valid @RequestBody ProfessionalSaveRequestDTO professionalSaveRequestDTO) {
        LOG.info("Creating professional client: [{}]", professionalSaveRequestDTO);
        return professionalService.saveProfessional(professionalSaveRequestDTO);
    }

    @GetMapping("/professional/{sirenNumber}")
    public ProfessionalResponseDTO getProfessionalClient(@PathVariable String sirenNumber) {
        LOG.info("Retrieving professional client with SIREN number: [{}]", sirenNumber);
        return professionalService.getProfessional(sirenNumber);
    }

    @PostMapping("/personal")
    @ResponseStatus(HttpStatus.CREATED)
    public PersonalClientResponseDTO createPersonalClient(@Valid @RequestBody PersonalSaveRequestDTO personalSaveRequestDTO) {
        LOG.info("Creating personal client: [{}]", personalSaveRequestDTO);
        return personalService.savePersonalClient(personalSaveRequestDTO);
    }

    @GetMapping("/personal/{username}")
    public PersonalClientResponseDTO getPersonalClient(@PathVariable String username) {
        LOG.info("Retrieving personal client with username: [{}]", username);
        return personalService.getPersonalClient(username);
    }
}

package org.capco.flexiprice.controller;

import jakarta.validation.Valid;
import org.capco.flexiprice.dto.PersonalClientResponseDTO;
import org.capco.flexiprice.dto.PersonalSaveRequestDTO;
import org.capco.flexiprice.dto.ProfessionalResponseDTO;
import org.capco.flexiprice.dto.ProfessionalSaveRequestDTO;
import org.capco.flexiprice.entity.client.PersonalClient;
import org.capco.flexiprice.service.client.PersonalService;
import org.capco.flexiprice.service.client.ProfessionalService;
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

    private final ProfessionalService professionalService;
    private final PersonalService personalService;

    public ClientController(ProfessionalService professionalService, PersonalService personalService) {
        this.professionalService = professionalService;
        this.personalService = personalService;
    }

    @PostMapping("/professional")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfessionalResponseDTO createProfessionalClient(@Valid @RequestBody ProfessionalSaveRequestDTO professionalSaveRequestDTO) {
        return professionalService.saveProfessional(professionalSaveRequestDTO);
    }

    @GetMapping("/professional/{sirenNumber}")
    public ProfessionalResponseDTO getProfessionalClient(@PathVariable String sirenNumber) {
        return professionalService.getProfessional(sirenNumber);
    }

    @PostMapping("/personal")
    @ResponseStatus(HttpStatus.CREATED)
    public PersonalClientResponseDTO createPersonalClient(@Valid @RequestBody PersonalSaveRequestDTO personalSaveRequestDTO) {
        PersonalClient personalClient = personalService.savePersonalClient(personalSaveRequestDTO);
        return PersonalClientResponseDTO.from(personalClient);
    }

    @GetMapping("/personal/{username}")
    public PersonalClientResponseDTO getPersonalClient(@PathVariable String username) {
        PersonalClient personalClient = personalService.getPersonalClient(username);
        return PersonalClientResponseDTO.from(personalClient);
    }
}

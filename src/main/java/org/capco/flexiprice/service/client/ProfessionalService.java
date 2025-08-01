package org.capco.flexiprice.service.client;

import org.capco.flexiprice.dto.ProfessionalSaveRequestDTO;
import org.capco.flexiprice.dto.ProfessionalResponseDTO;
import org.capco.flexiprice.entity.client.Professional;
import org.capco.flexiprice.exception.ProfessionalNotFoundException;
import org.capco.flexiprice.exception.SirenNumberAlreadyExistsException;
import org.capco.flexiprice.repository.client.ProfessionalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfessionalService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalService.class);

    private final ProfessionalRepository professionalRepository;

    public ProfessionalService(ProfessionalRepository professionalRepository) {
        this.professionalRepository = professionalRepository;
    }

    /**
     * Saves a new professional to the repository.
     * Checks for existing professionals with the same siren number and throws an exception if found.
     *
     * @param request the DTO containing professional data to save
     * @return the response DTO representing the saved professional
     * @throws SirenNumberAlreadyExistsException if a professional with the given siren number already exists
     */
    @Transactional
    public ProfessionalResponseDTO saveProfessional(ProfessionalSaveRequestDTO request) {
        LOG.info("Saving professional: {}", request);

        if (professionalRepository.existsProfessionalBySirenNumber(request.sirenNumber())) {
            LOG.error("Professional already exists for sirenNumber: {}", request.sirenNumber());
            throw new SirenNumberAlreadyExistsException("Siren number [" + request.sirenNumber() + "] already exists");
        }

        Professional professional = request.toProfessional();
        LOG.info("Client type: {}", professional.getClientType());
        Professional professionalSaved = professionalRepository.save(professional);

        LOG.info("Professional saved with id: {}", professionalSaved.getId());

        return ProfessionalResponseDTO.from(professionalSaved);
    }

    /**
     * Retrieves a professional by their siren number.
     *
     * @param sirenNumber the siren number of the professional to retrieve
     * @return the response DTO representing the found professional
     * @throws ProfessionalNotFoundException if no professional is found for the given siren number
     */
    @Transactional(readOnly = true)
    public ProfessionalResponseDTO getProfessional(String sirenNumber) {
        LOG.info("Getting professional by sirenNumber: {}", sirenNumber);
        return professionalRepository.findProfessionalBySirenNumber(sirenNumber)
                .map(ProfessionalResponseDTO::from)
                .orElseThrow(() -> {
                    LOG.error("Professional not found for sirenNumber: {}", sirenNumber);
                    return new ProfessionalNotFoundException("Professional not found for sirenNumber: " + sirenNumber);
                });
    }
}

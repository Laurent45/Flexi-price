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

@Service
public class ProfessionalService {

    private static final Logger logger = LoggerFactory.getLogger(ProfessionalService.class);

    private final ProfessionalRepository professionalRepository;

    public ProfessionalService(ProfessionalRepository professionalRepository) {
        this.professionalRepository = professionalRepository;
    }

    public ProfessionalResponseDTO saveProfessional(ProfessionalSaveRequestDTO request) {
        logger.info("Saving professional: {}", request);

        if (professionalRepository.existsProfessionalBySirenNumber(request.sirenNumber())) {
            logger.error("Professional already exists for sirenNumber: {}", request.sirenNumber());
            throw new SirenNumberAlreadyExistsException("Siren number [" + request.sirenNumber() + "] already exists");
        }

        Professional professional = request.toProfessional();
        logger.info("Client type: {}", professional.getClientType());
        Professional professionalSaved = professionalRepository.save(professional);

        logger.info("Professional saved with id: {}", professionalSaved.getId());

        return ProfessionalResponseDTO.from(professionalSaved);
    }

    public ProfessionalResponseDTO getProfessional(String sirenNumber) {
        logger.info("Getting professional by sirenNumber: {}", sirenNumber);
        return professionalRepository.findProfessionalBySirenNumber(sirenNumber)
                .map(ProfessionalResponseDTO::from)
                .orElseThrow(() -> {
                    logger.error("Professional not found for sirenNumber: {}", sirenNumber);
                    return new ProfessionalNotFoundException("Professional not found for sirenNumber: " + sirenNumber);
                });
    }
}

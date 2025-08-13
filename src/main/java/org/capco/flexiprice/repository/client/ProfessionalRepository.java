package org.capco.flexiprice.repository.client;

import org.capco.flexiprice.entity.client.ProfessionalClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessionalRepository extends JpaRepository<ProfessionalClient, Long> {

    boolean existsProfessionalBySirenNumber(String sirenNumber);

    Optional<ProfessionalClient> findProfessionalBySirenNumber(String sirenNumber);
}

package org.capco.flexiprice.repository.client;

import org.capco.flexiprice.entity.client.Professional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {

    boolean existsProfessionalBySirenNumber(String sirenNumber);

    Optional<Professional> findProfessionalBySirenNumber(String sirenNumber);
}

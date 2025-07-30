package org.capco.flexiprice.repository.client;

import org.capco.flexiprice.entity.client.PersonalClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalRepository extends JpaRepository<PersonalClient, Long> {

    Optional<PersonalClient> findByUsername(String username);

    boolean existsPersonalByUsername(String username);
}

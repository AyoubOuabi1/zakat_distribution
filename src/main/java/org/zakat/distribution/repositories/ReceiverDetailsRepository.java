package org.zakat.distribution.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zakat.distribution.entities.ReceiverDetails;

import java.util.Optional;

@Repository
public interface ReceiverDetailsRepository extends JpaRepository<ReceiverDetails, Long> {
    Optional<ReceiverDetails> findById(Long id);

    Optional<ReceiverDetails> findByUserId(Long id);
}

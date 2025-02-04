package org.zakat.distribution.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zakat.distribution.entities.Donation;
import org.zakat.distribution.entities.User;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByDonor(User donor);
}

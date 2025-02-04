package org.zakat.distribution.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zakat.distribution.entities.Donation;
import org.zakat.distribution.entities.User;
import org.zakat.distribution.repositories.DonationRepository;

import java.util.List;

@Service
public class DonationService {
    private final DonationRepository donationRepository;

    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public Donation addDonation(Donation donation) {
        return donationRepository.save(donation);
    }

    public List<Donation> getDonationHistory(User giver) {
        return donationRepository.findByDonor(giver);
    }
}

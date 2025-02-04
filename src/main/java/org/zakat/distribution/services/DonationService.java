package org.zakat.distribution.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zakat.distribution.dtos.DonationDTO;
import org.zakat.distribution.entities.Donation;
import org.zakat.distribution.entities.PaymentMethod;
import org.zakat.distribution.entities.User;
import org.zakat.distribution.repositories.DonationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationService {
    private final DonationRepository donationRepository;
    private final UserService userService;

    public DonationService(DonationRepository donationRepository,
                           UserService userService) {
        this.donationRepository = donationRepository;
        this.userService = userService;
    }


    public DonationDTO addDonation(DonationDTO donationDTO) {
        Donation donation = DonationDTO.toEntity(donationDTO,userService.getCurrentUser());
         donationRepository.save(donation);
         return DonationDTO.fromEntity(donation);
    }

    public List<DonationDTO> getDonationHistory() {
        List<Donation> donations = donationRepository.findByDonor(userService.getCurrentUser());
        return donations.stream()
                .map(DonationDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

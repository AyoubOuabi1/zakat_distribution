package org.zakat.distribution.services;

import org.springframework.stereotype.Service;
import org.zakat.distribution.dtos.DonationDTO;
import org.zakat.distribution.entities.Donation;
import org.zakat.distribution.entities.PaymentMethod;
import org.zakat.distribution.repositories.DonationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

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
        Donation donation = DonationDTO.toEntity(donationDTO, userService.getCurrentUser());
        donationRepository.save(donation);
        return DonationDTO.fromEntity(donation);
    }

    public List<DonationDTO> getDonationHistory() {
        List<Donation> donations = donationRepository.findByDonor(userService.getCurrentUser());
        return donations.stream()
                .map(DonationDTO::fromEntity)
                .toList();
    }

    @Transactional
    public DonationDTO updateDonation(Long id, DonationDTO donationDTO) {
        Donation existingDonation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        existingDonation.setAmount(donationDTO.getAmount());
        existingDonation.setPaymentMethod(PaymentMethod.valueOf(donationDTO.getPaymentMethod()));
        existingDonation.setPaymentDetails(donationDTO.getPaymentDetails());
        existingDonation.setDate(donationDTO.getDate());
        donationRepository.save(existingDonation);
        return DonationDTO.fromEntity(existingDonation);
    }

    @Transactional
    public void deleteDonation(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        donationRepository.delete(donation);
    }
}


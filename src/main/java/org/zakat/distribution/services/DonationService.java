package org.zakat.distribution.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zakat.distribution.dtos.DonationDTO;
import org.zakat.distribution.entities.Donation;
import org.zakat.distribution.entities.PaymentMethod;
import org.zakat.distribution.repositories.DonationRepository;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationService {
    private static final Logger logger = LoggerFactory.getLogger(DonationService.class);

    private final DonationRepository donationRepository;
    private final UserService userService;

    public DonationService(DonationRepository donationRepository,
                           UserService userService) {
        this.donationRepository = donationRepository;
        this.userService = userService;
        logger.info("DonationService initialized with DonationRepository and UserService.");
    }

    public DonationDTO addDonation(DonationDTO donationDTO) {
        logger.info("Adding a new donation for user: {}", userService.getCurrentUser().getFullName());

        Donation donation = DonationDTO.toEntity(donationDTO, userService.getCurrentUser());
        donationRepository.save(donation);
        logger.debug("Donation saved successfully with ID: {}", donation.getId());

        logger.info("Donation added successfully for user: {}", userService.getCurrentUser().getFullName());
        return DonationDTO.fromEntity(donation);
    }

    public List<DonationDTO> getDonationHistory() {
        logger.info("Fetching donation history for user: {}", userService.getCurrentUser().getFullName());

        List<Donation> donations = donationRepository.findByDonor(userService.getCurrentUser());
        logger.debug("Retrieved {} donations for user: {}", donations.size(), userService.getCurrentUser().getFullName());

        List<DonationDTO> donationDTOs = donations.stream()
                .map(DonationDTO::fromEntity)
                .toList();

        logger.info("Successfully fetched donation history for user: {}", userService.getCurrentUser().getFullName());
        return donationDTOs;
    }

    @Transactional
    public DonationDTO updateDonation(Long id, DonationDTO donationDTO) {
        logger.info("Updating donation with ID: {}", id);

        Donation existingDonation = donationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Donation not found with ID: {}", id);
                    return new RuntimeException("Donation not found");
                });

        existingDonation.setAmount(donationDTO.getAmount());
        existingDonation.setPaymentMethod(PaymentMethod.valueOf(donationDTO.getPaymentMethod()));
        existingDonation.setPaymentDetails(donationDTO.getPaymentDetails());
        existingDonation.setDate(donationDTO.getDate());

        donationRepository.save(existingDonation);
        logger.debug("Donation updated successfully with ID: {}", id);

        logger.info("Donation with ID: {} updated successfully.", id);
        return DonationDTO.fromEntity(existingDonation);
    }

    @Transactional
    public void deleteDonation(Long id) {
        logger.info("Deleting donation with ID: {}", id);

        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Donation not found with ID: {}", id);
                    return new RuntimeException("Donation not found");
                });

        donationRepository.delete(donation);
        logger.debug("Donation deleted successfully with ID: {}", id);

        logger.info("Donation with ID: {} deleted successfully.", id);
    }

    public List<DonationDTO> getAllDonationsWithDonorFullName() {
        logger.info("Fetching all donations with donor full names...");

        List<Donation> donations = donationRepository.findAll();
        logger.debug("Retrieved {} donations from the repository.", donations.size());

        List<DonationDTO> donationDTOs = donations.stream()
                .map(DonationDTO::fromEntity)
                .toList();

        logger.info("Successfully fetched {} donations with donor full names.", donationDTOs.size());
        return donationDTOs;
    }
}
package org.zakat.distribution.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zakat.distribution.dtos.DonationDTO;
import org.zakat.distribution.services.DonationService;

import java.util.List;

@RestController
@RequestMapping("/api/donation")
public class DonationController {
    private static final Logger logger = LoggerFactory.getLogger(DonationController.class);

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
        logger.info("DonationController initialized with DonationService.");
    }

    @PostMapping
    public ResponseEntity<DonationDTO> addDonation(@RequestBody DonationDTO donationDTO) {
        logger.info("Received request to add a new donation.");
        DonationDTO addedDonation = donationService.addDonation(donationDTO);
        logger.info("Donation added successfully with ID: {}", addedDonation.getId());
        return ResponseEntity.ok(addedDonation);
    }

    @GetMapping("/history")
    public ResponseEntity<List<DonationDTO>> getDonationHistory() {
        logger.info("Received request to fetch donation history.");
        List<DonationDTO> donationHistory = donationService.getDonationHistory();
        logger.info("Successfully fetched {} donations in history.", donationHistory.size());
        return ResponseEntity.ok(donationHistory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonationDTO> updateDonation(@PathVariable Long id, @RequestBody DonationDTO donationDTO) {
        logger.info("Received request to update donation with ID: {}", id);
        DonationDTO updatedDonation = donationService.updateDonation(id, donationDTO);
        logger.info("Donation with ID: {} updated successfully.", id);
        return ResponseEntity.ok(updatedDonation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable Long id) {
        logger.info("Received request to delete donation with ID: {}", id);
        donationService.deleteDonation(id);
        logger.info("Donation with ID: {} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<DonationDTO>> getAllDonationsWithDonorFullName() {
        logger.info("Received request to fetch all donations with donor full names.");
        List<DonationDTO> donations = donationService.getAllDonationsWithDonorFullName();
        logger.info("Successfully fetched {} donations with donor full names.", donations.size());
        return ResponseEntity.ok(donations);
    }
}
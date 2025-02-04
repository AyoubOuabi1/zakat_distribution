package org.zakat.distribution.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zakat.distribution.dtos.DonationDTO;
import org.zakat.distribution.services.DonationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/donation")
public class DonorController {

    private final DonationService donationService;
    public DonorController(final DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping()
    public ResponseEntity<DonationDTO> createDonation(@RequestBody final DonationDTO donationDTO) {
        return ResponseEntity.ok(donationService.addDonation(donationDTO));
    }

    @GetMapping()
    public ResponseEntity<List<DonationDTO>> getDonationHistory() {
        List<DonationDTO> donationDTOs = donationService.getDonationHistory();
        return ResponseEntity.ok(donationDTOs);
    }
}

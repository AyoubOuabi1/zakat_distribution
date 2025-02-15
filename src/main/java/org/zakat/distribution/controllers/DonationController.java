package org.zakat.distribution.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zakat.distribution.dtos.DonationDTO;
import org.zakat.distribution.services.DonationService;

import java.util.List;

@RestController
@RequestMapping("/api/donation")
public class DonationController {
    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping
    public ResponseEntity<DonationDTO> addDonation(@RequestBody DonationDTO donationDTO) {
        return ResponseEntity.ok(donationService.addDonation(donationDTO));
    }

    @GetMapping("/history")
    public ResponseEntity<List<DonationDTO>> getDonationHistory() {
        return ResponseEntity.ok(donationService.getDonationHistory());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonationDTO> updateDonation(@PathVariable Long id, @RequestBody DonationDTO donationDTO) {
        return ResponseEntity.ok(donationService.updateDonation(id, donationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable Long id) {
        donationService.deleteDonation(id);
        return ResponseEntity.noContent().build();
    }
}


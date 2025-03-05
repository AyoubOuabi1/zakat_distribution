package org.zakat.distribution.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zakat.distribution.dtos.ZakatDTO;
import org.zakat.distribution.services.ZakatService;

import java.util.List;

@RestController
@RequestMapping("/api/zakat")
public class ZakatController {
    private static final Logger logger = LoggerFactory.getLogger(ZakatController.class);

    private final ZakatService zakatService;

    public ZakatController(ZakatService zakatService) {
        this.zakatService = zakatService;
        logger.info("ZakatController initialized with ZakatService.");
    }

    @GetMapping("/history")
    public ResponseEntity<List<ZakatDTO>> getReceivedZakatHistory() {
        logger.info("Received request to fetch received Zakat history.");

        List<ZakatDTO> zakatHistory = zakatService.getReceivedZakatHistory();
        logger.info("Successfully fetched {} Zakat entries in history.", zakatHistory.size());

        return ResponseEntity.ok(zakatHistory);
    }

    @PostMapping("")
    public ResponseEntity<ZakatDTO> addZakat(@RequestBody ZakatDTO zakatDTO) {
        logger.info("Received request to add a new Zakat entry.");

        ZakatDTO savedZakat = zakatService.addZakat(zakatDTO);
        logger.info("Zakat entry added successfully with ID: {}", savedZakat.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedZakat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZakatDTO> updateZakat(@PathVariable Long id, @RequestBody ZakatDTO zakatDTO) {
        logger.info("Received request to update Zakat entry with ID: {}", id);
        ZakatDTO updatedZakat = zakatService.updateZakat(id, zakatDTO);
        logger.info("Zakat entry with ID: {} updated successfully.", id);

        return ResponseEntity.ok(updatedZakat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZakat(@PathVariable Long id) {
        logger.info("Received request to delete Zakat entry with ID: {}", id);

        zakatService.deleteZakat(id);
        logger.info("Zakat entry with ID: {} deleted successfully.", id);

        return ResponseEntity.noContent().build();
    }
}
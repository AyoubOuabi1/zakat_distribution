package org.zakat.distribution.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zakat.distribution.dtos.ZakatDTO;
import org.zakat.distribution.entities.Zakat;
import org.zakat.distribution.services.ZakatService;

import java.util.List;

@RestController
@RequestMapping("/api/zakat")
public class ZakatController {
    private final ZakatService zakatService;

    public ZakatController(ZakatService zakatService) {
        this.zakatService = zakatService;
    }

    @GetMapping("/history")
    public ResponseEntity<List<ZakatDTO>> getReceivedZakatHistory() {
        List<ZakatDTO> zakatHistory = zakatService.getReceivedZakatHistory();
        return ResponseEntity.ok(zakatHistory);
    }

    @PostMapping("")
    public ResponseEntity<ZakatDTO> addZakat(@RequestBody ZakatDTO zakatDTO) {
        ZakatDTO savedZakat = zakatService.addZakat(zakatDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedZakat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZakatDTO> updateZakat(@PathVariable Long id, @RequestBody ZakatDTO zakatDTO) {
        ZakatDTO updatedZakat = zakatService.updateZakat(id, zakatDTO);
        return ResponseEntity.ok(updatedZakat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZakat(@PathVariable Long id) {
        zakatService.deleteZakat(id);
        return ResponseEntity.noContent().build();
    }
}

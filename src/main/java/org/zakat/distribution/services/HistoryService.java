package org.zakat.distribution.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zakat.distribution.dtos.HistoryItemDTO;
import org.zakat.distribution.repositories.DonationRepository;
import org.zakat.distribution.repositories.ZakatRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HistoryService {

    private final ZakatRepository zakatRepository;
    private final DonationRepository donationRepository;

    @Autowired
    public HistoryService(ZakatRepository zakatRepository, DonationRepository donationRepository) {
        this.zakatRepository = zakatRepository;
        this.donationRepository = donationRepository;
    }

    public List<HistoryItemDTO> getCombinedHistory() {
        // Convert zakat records to DTOs
        var zakatStream = zakatRepository.findAll().stream()
                .map(zakat -> {
                    String paymentMethod = zakat.getReceiver() != null &&
                            zakat.getReceiver().getReceiverDetails() != null ?
                            zakat.getReceiver().getReceiverDetails().getPaymentMethod().name() :
                            "UNKNOWN";

                    return new HistoryItemDTO(
                            zakat.getId(),
                            "ZAKAT",
                            zakat.getReceiver() != null ? zakat.getReceiver().getFullName() : "Unknown",
                            zakat.getAmountReceived(),
                            zakat.getDateReceived(),
                            paymentMethod
                    );
                });

        // Convert donation records to DTOs
        var donationStream = donationRepository.findAll().stream()
                .map(donation -> new HistoryItemDTO(
                        donation.getId(),
                        "DONATION",
                        donation.getDonor() != null ? donation.getDonor().getFullName() : "Unknown",
                        donation.getAmount(),
                        donation.getDate(),
                        donation.getPaymentMethod() != null ?
                                donation.getPaymentMethod().name() : "UNKNOWN"
                ));

        // Combine and sort by date (newest first)
        return Stream.concat(zakatStream, donationStream)
                .sorted(Comparator.comparing(HistoryItemDTO::getDate).reversed())
                .collect(Collectors.toList());
    }
}
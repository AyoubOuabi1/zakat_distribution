package org.zakat.distribution.dtos;

import lombok.Getter;
import lombok.Setter;
import org.zakat.distribution.entities.Donation;
import org.zakat.distribution.entities.PaymentMethod;
import org.zakat.distribution.entities.User;

import java.time.LocalDate;


public class DonationDTO {
    private Long id;
    private Double amount;
    private String paymentMethod;
    private String paymentDetails;
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DonationDTO(Long id, Double amount, String paymentMethod, String paymentDetails, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDetails = paymentDetails;
        this.date = date;
    }

    public static DonationDTO fromEntity(Donation donation) {
        return new DonationDTO(
                donation.getId(),
                donation.getAmount(),
                donation.getPaymentMethod().toString(),
                donation.getPaymentDetails(),
                donation.getDate()
        );
    }

    public static Donation toEntity(DonationDTO donationDTO, User donor) {
        Donation donation = new Donation();
        donation.setId(donationDTO.getId());
        donation.setAmount(donationDTO.getAmount());
        donation.setPaymentMethod(PaymentMethod.valueOf(donationDTO.getPaymentMethod()));
        donation.setPaymentDetails(donationDTO.getPaymentDetails());
        donation.setDate(LocalDate.now());
        donation.setDonor(donor);
        return donation;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}


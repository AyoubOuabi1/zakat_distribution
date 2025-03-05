package org.zakat.distribution.dtos;
import jakarta.annotation.Nullable;
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
    private String donorFullName;
    public DonationDTO() {
    }
    public DonationDTO(Long id, Double amount, String paymentMethod, String paymentDetails, LocalDate date, String donorFullName) {
        this.id = id;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDetails = paymentDetails;
        this.date = date;
        this.donorFullName = donorFullName;
    }
    public DonationDTO(Double amount, String paymentMethod, String paymentDetails, LocalDate date) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDetails = paymentDetails;
        this.date = date;
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
                donation.getDate(),
                donation.getDonor().getFullName()
        );
    }

    public static Donation toEntity(DonationDTO donationDTO, User donor) {
        Donation donation = new Donation();
        donation.setId(donationDTO.getId());
        donation.setAmount(donationDTO.getAmount());
        donation.setPaymentMethod(PaymentMethod.valueOf(donationDTO.getPaymentMethod()));
        donation.setPaymentDetails(donationDTO.getPaymentDetails());
        donation.setDate(donationDTO.getDate());
        donation.setDonor(donor);
        return donation;
    }
    public String getDonorFullName() {
        return donorFullName;
    }

    public void setDonorFullName(String donorFullName) {
        this.donorFullName = donorFullName;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


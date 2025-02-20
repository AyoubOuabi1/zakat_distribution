package org.zakat.distribution.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.zakat.distribution.entities.PaymentMethod;
import org.zakat.distribution.entities.ReceiverDetails;
import org.zakat.distribution.entities.Role;
import org.zakat.distribution.entities.User;

public class UserDTO {
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Please provide a valid phone number")
    private String phoneNumber;

    @NotBlank(message = "Canton is required")
    private String canton;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "^[0-9]{4}$", message = "Please provide a valid  postal code")
    private String postalCode;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(DONOR|RECEIVER|ADMIN)$", message = "Invalid role")
    private String role;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$",
            message = "Password must contain at least one letter and one number")
    private String newPassword;

    private String confirmNewPassword;

    private PaymentMethod paymentMethod;

    private String bankDetailsImage;

    private Double totalDonated; // New field
    private Double totalReceived; // New field

    // Constructor with all fields
    public UserDTO(Long id, String fullName, String email, String address, String phoneNumber, String canton, String postalCode, String role, String newPassword, String confirmNewPassword, PaymentMethod paymentMethod, String bankDetailsImage, Double totalDonated, Double totalReceived) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.canton = canton;
        this.postalCode = postalCode;
        this.role = role;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
        this.paymentMethod = paymentMethod;
        this.bankDetailsImage = bankDetailsImage;
        this.totalDonated = totalDonated;
        this.totalReceived = totalReceived;
    }

    // Getters and setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBankDetailsImage() {
        return bankDetailsImage;
    }

    public void setBankDetailsImage(String bankDetailsImage) {
        this.bankDetailsImage = bankDetailsImage;
    }

    public Double getTotalDonated() {
        return totalDonated;
    }

    public void setTotalDonated(Double totalDonated) {
        this.totalDonated = totalDonated;
    }

    public Double getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(Double totalReceived) {
        this.totalReceived = totalReceived;
    }

    // Default constructor
    public UserDTO() {}

    // Static method to create UserDTO from User entity
    public static UserDTO fromEntity(User user, Double totalDonated, Double totalReceived) {
        ReceiverDetails receiverDetails = user.getReceiverDetails();
        return new UserDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getCanton(),
                user.getPostalCode(),
                user.getRole().toString(),
                null,
                null,
                receiverDetails != null ? receiverDetails.getPaymentMethod() : null,
                receiverDetails != null ? receiverDetails.getBankDetailsImage() : null,
                totalDonated,
                totalReceived
        );
    }

    public void validatePasswordMatch() {
        if (newPassword != null && confirmNewPassword != null && !newPassword.equals(confirmNewPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }

    public void validateReceiverFields() {
        if (Role.RECEIVER.name().equals(role.toUpperCase())) {
            if (paymentMethod == null) {
                throw new IllegalArgumentException("Payment method is required for receivers");
            }

            if (paymentMethod == PaymentMethod.BANK_TRANSFER && (bankDetailsImage == null || bankDetailsImage.isEmpty())) {
                throw new IllegalArgumentException("Bank details image is required for bank transfer payment method");
            }
        }
    }
}
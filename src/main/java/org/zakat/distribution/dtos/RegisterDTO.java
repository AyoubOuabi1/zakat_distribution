package org.zakat.distribution.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.zakat.distribution.entities.Role;
import org.zakat.distribution.entities.User;
import org.zakat.distribution.entities.ReceiverDetails;
import org.zakat.distribution.entities.PaymentMethod;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
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
    @Pattern(regexp = "^[0-9]{4,10}$", message = "Please provide a valid  postal code")
    private String postalCode;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(DONOR|RECEIVER|ADMIN)$", message = "Invalid role")
    private String role;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$",
            message = "Password must contain at least one letter and one number")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;
    private PaymentMethod paymentMethod;
    private MultipartFile bankDetailsImage;

    public static User toEntity(RegisterDTO dto, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail().toLowerCase());
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setCanton(dto.getCanton());
        user.setPostalCode(dto.getPostalCode());
        user.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (user.getRole() == Role.RECEIVER) {
            validateReceiverDetails(dto);
            ReceiverDetails receiverDetails = new ReceiverDetails();
            receiverDetails.setPaymentMethod(dto.getPaymentMethod());
            receiverDetails.setUser(user);
            user.setReceiverDetails(receiverDetails);
        }

        return user;
    }

    private static void validateReceiverDetails(RegisterDTO dto) {
        if (dto.getPaymentMethod() == null) {
            throw new IllegalArgumentException("Payment method is required for receivers");
        }

        if (dto.getPaymentMethod() == PaymentMethod.BANK_TRANSFER &&
                (dto.getBankDetailsImage() == null || dto.getBankDetailsImage().isEmpty())) {
            throw new IllegalArgumentException("Bank details image is required for bank transfer payment method");
        }
    }

    public void validatePasswordMatch() {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }

    public void validateBankDetailsImage() {
        if (bankDetailsImage != null && !bankDetailsImage.isEmpty()) {
            // Check file size (e.g., max 5MB)
            if (bankDetailsImage.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("File size should not exceed 5MB");
            }

            // Check file type
            /*String contentType = bankDetailsImage.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed");
            }*/
        }
    }

    public void validateReceiverFields() {
        if (Role.RECEIVER.name().equals(role.toUpperCase())) {
            if (paymentMethod == null) {
                throw new IllegalArgumentException("Payment method is required for receivers");
            }

            if (paymentMethod == PaymentMethod.BANK_TRANSFER) {
                validateBankDetailsImage();
            }
        }
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public MultipartFile getBankDetailsImage() {
        return bankDetailsImage;
    }

    public void setBankDetailsImage(MultipartFile bankDetailsImage) {
        this.bankDetailsImage = bankDetailsImage;
    }
}
package org.zakat.distribution.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.zakat.distribution.entities.Role;
import org.zakat.distribution.entities.User;
import org.zakat.distribution.entities.ReceiverDetails;
import org.zakat.distribution.entities.PaymentMethod;


public class RegisterDTO {
    private String fullName;
    private String email;
    private String address;
    private String phoneNumber;
    private String canton;
    private String postalCode;
    private String role;
    private String password;
    private String confirmPassword;

    // Receiver-specific fields
    private PaymentMethod paymentMethod;
    private MultipartFile bankDetailsImage;

    public RegisterDTO() {}

    public RegisterDTO(String fullName, String email, String address, String phoneNumber, String canton, String postalCode, String role, String password, String confirmPassword, PaymentMethod paymentMethod, MultipartFile  bankDetailsImage) {
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.canton = canton;
        this.postalCode = postalCode;
        this.role = role;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.paymentMethod = paymentMethod;
        this.bankDetailsImage = bankDetailsImage;
    }

    public static User toEntity(RegisterDTO dto, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setCanton(dto.getCanton());
        user.setPostalCode(dto.getPostalCode());
        user.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (user.getRole() == Role.RECEIVER) {
            ReceiverDetails receiverDetails = new ReceiverDetails();
            receiverDetails.setPaymentMethod(dto.getPaymentMethod());
            receiverDetails.setUser(user); // Set the user for receiverDetails
            user.setReceiverDetails(receiverDetails); // Set receiverDetails for user
        }

        return user;
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

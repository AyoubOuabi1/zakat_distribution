package org.zakat.distribution.dtos;


import org.zakat.distribution.entities.User;
import org.zakat.distribution.entities.PaymentMethod;
import org.zakat.distribution.entities.ReceiverDetails;

public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String address;

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

    private String phoneNumber;
    private String canton;
    private String postalCode;
    private String role;
    private String newPassword;
    private String confirmNewPassword;
    private PaymentMethod paymentMethod;
    private String bankDetailsImage;
    public UserDTO() {}

    public UserDTO(Long id, String fullName, String email, String address, String phoneNumber, String canton, String postalCode, String role, String newPassword, String confirmNewPassword, PaymentMethod paymentMethod, String bankDetailsImage) {
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
    }

    public static UserDTO fromEntity(User user) {
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
                receiverDetails != null ? receiverDetails.getBankDetailsImage() : null
        );
    }

}

package org.zakat.distribution.dtos;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zakat.distribution.entities.Role;
import org.zakat.distribution.entities.User;

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

    public RegisterDTO() {}

    public RegisterDTO(String fullName, String email, String address, String phoneNumber, String canton, String postalCode, String role, String password, String confirmPassword) {
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.canton = canton;
        this.postalCode = postalCode;
        this.role = role;
        this.password = password;
        this.confirmPassword = confirmPassword;
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
}



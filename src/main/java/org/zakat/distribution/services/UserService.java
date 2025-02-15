package org.zakat.distribution.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zakat.distribution.dtos.RegisterDTO;
import org.zakat.distribution.dtos.UserDTO;
import org.zakat.distribution.entities.Role;
import org.zakat.distribution.entities.User;
import org.zakat.distribution.exceptions.ResourceNotFoundException;
import org.zakat.distribution.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDTO::fromEntity)
                .toList();
    }
    public UserDTO registerUser(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }
        User user = RegisterDTO.toEntity(registerDTO,passwordEncoder);
        userRepository.save(user);
        return UserDTO.fromEntity(user);
    }

    public UserDTO getUserByEmail(String email) {
        return UserDTO.fromEntity(userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails)principal).getUsername();
            return userRepository.findByEmail(username).get();
        }

        return null;
    }
    @Transactional
    public UserDTO updateCurrentUser(UserDTO userDTO) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find current user by email
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        // Validate email uniqueness
        if (!currentUser.getEmail().equals(userDTO.getEmail()) &&
                userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        // Update user information (excluding password)
        currentUser.setFullName(userDTO.getFullName());
        currentUser.setEmail(userDTO.getEmail());
        currentUser.setAddress(userDTO.getAddress());
        currentUser.setPhoneNumber(userDTO.getPhoneNumber());
        currentUser.setCanton(userDTO.getCanton());
        currentUser.setPostalCode(userDTO.getPostalCode());
        currentUser.setRole(Role.valueOf(userDTO.getRole()));

        // If a new password is provided, validate and update it
        if (userDTO.getNewPassword() != null && !userDTO.getNewPassword().isEmpty()) {
            // Check if passwords match
            if (!userDTO.getNewPassword().equals(userDTO.getConfirmNewPassword())) {
                throw new IllegalStateException("Passwords do not match");
            }

            // Encode the password before saving
            currentUser.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        }

        // Save the updated user
        User updatedUser = userRepository.save(currentUser);
        return UserDTO.fromEntity(updatedUser);
    }


}


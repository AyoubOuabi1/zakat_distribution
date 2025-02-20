package org.zakat.distribution.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zakat.distribution.dtos.RegisterDTO;
import org.zakat.distribution.dtos.UserDTO;
import org.zakat.distribution.entities.ReceiverDetails;
import org.zakat.distribution.entities.Role;
import org.zakat.distribution.entities.User;
import org.zakat.distribution.exceptions.ResourceNotFoundException;
import org.zakat.distribution.repositories.ReceiverDetailsRepository;
import org.zakat.distribution.repositories.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ReceiverDetailsRepository receiverDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ReceiverDetailsRepository receiverDetailsRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.receiverDetailsRepository = receiverDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDTO::fromEntity)
                .toList();
    }
    public UserDTO registerUser(RegisterDTO registerDTO) {
        validateRegistration(registerDTO);
        User user = RegisterDTO.toEntity(registerDTO, passwordEncoder);
        user = userRepository.save(user);
        if (user.getRole() == Role.RECEIVER) {
            ReceiverDetails receiverDetails = receiverDetailsRepository.findById(user.getId())
                    .orElse(new ReceiverDetails());
            receiverDetails.setPaymentMethod(registerDTO.getPaymentMethod());
            if (registerDTO.getBankDetailsImage() != null && !registerDTO.getBankDetailsImage().isEmpty()) {
                String resourcesDir = "src/main/resources/uploads/bank-details/";
                Path uploadDir = Paths.get(resourcesDir);

                if (!Files.exists(uploadDir)) {
                    try {
                        Files.createDirectories(uploadDir);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to create upload directory", e);
                    }
                }
                String fileName = UUID.randomUUID()+ "_" + registerDTO.getBankDetailsImage().getOriginalFilename();
                Path filePath = uploadDir.resolve(fileName);
                if (!Files.exists(filePath)) {
                    try {
                        Files.copy(registerDTO.getBankDetailsImage().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                        receiverDetails.setBankDetailsImage(fileName);
                    } catch (IOException e) {
                        throw new RuntimeException("Error saving bank details image", e);
                    }
                } else {
                    receiverDetails.setBankDetailsImage(filePath.getFileName().toString());
                }
            }
            receiverDetails.setUser(user);
            receiverDetailsRepository.save(receiverDetails);
        }

        return UserDTO.fromEntity(user);
    }
    private void validateRegistration(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }
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

            currentUser.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        }

        User updatedUser = userRepository.save(currentUser);
        return UserDTO.fromEntity(updatedUser);
    }


}


package org.zakat.distribution.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zakat.distribution.dtos.RegisterDTO;
import org.zakat.distribution.dtos.UserDTO;
import org.zakat.distribution.entities.*;
import org.zakat.distribution.exceptions.ResourceNotFoundException;
import org.zakat.distribution.repositories.ReceiverDetailsRepository;
import org.zakat.distribution.repositories.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final String UPLOAD_DIR = "src/main/resources/uploads/bank-details/";

    private final UserRepository userRepository;
    private final ReceiverDetailsRepository receiverDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    @PersistenceContext
    private EntityManager entityManager;
    public UserService(UserRepository userRepository, ReceiverDetailsRepository receiverDetailsRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.receiverDetailsRepository = receiverDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAllUsersWithTotals().stream()
                .map(result -> UserDTO.fromEntity(
                        (User) result[0],
                        (Double) result[1],
                        (Double) result[2]))
                .toList();
    }

    @Transactional
    public void registerUser(RegisterDTO registerDTO) {
        validateRegistration(registerDTO);
        User user = RegisterDTO.toEntity(registerDTO, passwordEncoder);
        user = userRepository.save(user);
        if (user.getRole() == Role.RECEIVER) {
            saveReceiverDetails(user, registerDTO);
        }
    }


    @Transactional
    public UserDTO updateCurrentUser(UserDTO userDTO, MultipartFile bankDetailsImage) {
        User currentUser = getCurrentUserFromContext();
        validateEmailUpdate(currentUser, userDTO.getEmail());

        updateUserBasicInfo(currentUser, userDTO);
        updatePasswordIfProvided(currentUser, userDTO);

        if (currentUser.getRole() == Role.RECEIVER) {
            updateReceiverDetails(currentUser, userDTO, bankDetailsImage);
        }

        User updatedUser = userRepository.save(currentUser);
        return createUserDTOWithTotals(updatedUser);
    }

    public User getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(auth -> {
                    if (auth.getPrincipal() instanceof UserDetails) {
                        String username = ((UserDetails) auth.getPrincipal()).getUsername();
                        return userRepository.findByEmail(username)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    }
                    return null;
                })
                .orElse(null);
    }

    // Private helper methods
    private void validateRegistration(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }
    }

    private void saveReceiverDetails(User user, RegisterDTO registerDTO) {
        ReceiverDetails receiverDetails = new ReceiverDetails();
        receiverDetails.setUser(user);
        receiverDetails.setPaymentMethod(registerDTO.getPaymentMethod());

        if (registerDTO.getBankDetailsImage() != null && !registerDTO.getBankDetailsImage().isEmpty()) {
            String fileName = saveImageFile(registerDTO.getBankDetailsImage());
            receiverDetails.setBankDetailsImage(fileName);
        }

        receiverDetailsRepository.save(receiverDetails);
    }


    private String saveImageFile(MultipartFile imageFile) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image file", e);
        }
    }

    private User getCurrentUserFromContext() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }

    private void validateEmailUpdate(User currentUser, String newEmail) {
        if (!currentUser.getEmail().equals(newEmail) &&
                userRepository.findByEmail(newEmail).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }
    }

    private void updateUserBasicInfo(User user, UserDTO userDTO) {
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setCanton(userDTO.getCanton());
        user.setPostalCode(userDTO.getPostalCode());
        user.setRole(Role.valueOf(userDTO.getRole()));
    }

    private void updatePasswordIfProvided(User user, UserDTO userDTO) {
        if (userDTO.getNewPassword() != null && !userDTO.getNewPassword().isEmpty()) {
            if (!userDTO.getNewPassword().equals(userDTO.getConfirmNewPassword())) {
                throw new IllegalStateException("Passwords do not match");
            }
            user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        }
    }

    private void updateReceiverDetails(User user, UserDTO userDTO, MultipartFile bankDetailsImage) {
        ReceiverDetails receiverDetails = user.getReceiverDetails();
        if (receiverDetails == null) {
            receiverDetails = new ReceiverDetails();
            receiverDetails.setUser(user);
        }

        PaymentMethod paymentMethod = PaymentMethod.valueOf(userDTO.getPaymentMethod().name());
        receiverDetails.setPaymentMethod(paymentMethod);

        if (paymentMethod == PaymentMethod.TWINT) {
            receiverDetails.setBankDetailsImage(null);
        } else if (bankDetailsImage != null && !bankDetailsImage.isEmpty()) {
            String fileName = saveImageFile(bankDetailsImage);
            receiverDetails.setBankDetailsImage(fileName);
        }

        receiverDetailsRepository.save(receiverDetails);
    }

    private UserDTO createUserDTOWithTotals(User user) {
        Double totalDonated = user.getDonationsHistory().stream()
                .mapToDouble(Donation::getAmount)
                .sum();

        Double totalReceived = user.getZakatHistory().stream()
                .mapToDouble(Zakat::getAmountReceived)
                .sum();

        return UserDTO.fromEntity(user, totalDonated, totalReceived);
    }
}
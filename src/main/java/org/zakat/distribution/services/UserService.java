package org.zakat.distribution.services;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final String UPLOAD_DIR = "/var/www/html/aktion/";
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final ReceiverDetailsRepository receiverDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ReceiverDetailsRepository receiverDetailsRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.receiverDetailsRepository = receiverDetailsRepository;
        this.passwordEncoder = passwordEncoder;
        logger.info("UserService initialized with dependencies.");
    }

    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users with totals.");
        List<UserDTO> users = userRepository.findAllUsersWithTotals().stream()
                .map(result -> UserDTO.fromEntity(
                        (User) result[0],
                        (Double) result[1],
                        (Double) result[2]))
                .toList();
        logger.debug("Fetched {} users.", users.size());
        return users;
    }

    @Transactional
    public void registerUser(RegisterDTO registerDTO) {
        logger.info("Attempting to register user with email: {}", registerDTO.getEmail());
        validateRegistration(registerDTO);
        User user = RegisterDTO.toEntity(registerDTO, passwordEncoder);
        user = userRepository.save(user);
        logger.info("User registered successfully with ID: {}", user.getId());

        if (user.getRole() == Role.RECEIVER) {
            logger.debug("User role is RECEIVER. Saving receiver details.");
            saveReceiverDetails(user, registerDTO);
        }
    }

    @Transactional
    public UserDTO updateCurrentUser(UserDTO userDTO, MultipartFile bankDetailsImage) {
        logger.info("Updating current user with email: {}", userDTO.getEmail());
        User currentUser = getCurrentUserFromContext();
        validateEmailUpdate(currentUser, userDTO.getEmail());

        updateUserBasicInfo(currentUser, userDTO);
        updatePasswordIfProvided(currentUser, userDTO);

        if (currentUser.getRole() == Role.RECEIVER) {
            logger.debug("User role is RECEIVER. Updating receiver details.");
            updateReceiverDetails(currentUser, userDTO, bankDetailsImage);
        }

        User updatedUser = userRepository.save(currentUser);
        logger.info("User updated successfully with ID: {}", updatedUser.getId());
        return createUserDTOWithTotals(updatedUser);
    }

    public User getCurrentUser() {
        logger.info("Fetching current user from security context.");
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(auth -> {
                    if (auth.getPrincipal() instanceof UserDetails) {
                        String username = ((UserDetails) auth.getPrincipal()).getUsername();
                        logger.debug("Current user email: {}", username);
                        return userRepository.findByEmail(username)
                                .orElseThrow(() -> {
                                    logger.error("User not found with email: {}", username);
                                    return new ResourceNotFoundException("User not found");
                                });
                    }
                    return null;
                })
                .orElse(null);
    }

    private void validateRegistration(RegisterDTO registerDTO) {
        logger.debug("Validating registration for email: {}", registerDTO.getEmail());
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            logger.error("Email already in use: {}", registerDTO.getEmail());
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            logger.error("Passwords do not match for email: {}", registerDTO.getEmail());
            throw new IllegalArgumentException("Passwords do not match.");
        }
    }

    private void saveReceiverDetails(User user, RegisterDTO registerDTO) {
        logger.debug("Saving receiver details for user ID: {}", user.getId());
        Optional<ReceiverDetails> existingDetails = receiverDetailsRepository.findByUserId(user.getId());
        ReceiverDetails receiverDetails = existingDetails.orElse(new ReceiverDetails());
        receiverDetails.setUser(user);
        receiverDetails.setPaymentMethod(registerDTO.getPaymentMethod());

        if (registerDTO.getBankDetailsImage() != null && !registerDTO.getBankDetailsImage().isEmpty()) {
            logger.debug("Saving bank details image for user ID: {}", user.getId());
            String fileName = saveImageFile(registerDTO.getBankDetailsImage());
            receiverDetails.setBankDetailsImage(fileName);
        }

        receiverDetailsRepository.save(receiverDetails);
        logger.info("Receiver details saved successfully for user ID: {}", user.getId());
    }

    private String saveImageFile(MultipartFile imageFile) {
        try {
            logger.debug("Saving image file: {}", imageFile.getOriginalFilename());
            Path uploadPath = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Image file saved successfully: {}", filePath);
            return fileName;
        } catch (IOException e) {
            logger.error("Failed to save image file: {}", imageFile.getOriginalFilename(), e);
            throw new RuntimeException("Failed to save image file", e);
        }
    }

    private User getCurrentUserFromContext() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug("Fetching current user from context with email: {}", currentUserEmail);
        return userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> {
                    logger.error("Current user not found with email: {}", currentUserEmail);
                    return new ResourceNotFoundException("Current user not found");
                });
    }

    private void validateEmailUpdate(User currentUser, String newEmail) {
        logger.debug("Validating email update for user ID: {}", currentUser.getId());
        if (!currentUser.getEmail().equals(newEmail) &&
                userRepository.findByEmail(newEmail).isPresent()) {
            logger.error("Email already exists: {}", newEmail);
            throw new IllegalStateException("Email already exists");
        }
    }

    private void updateUserBasicInfo(User user, UserDTO userDTO) {
        logger.debug("Updating basic info for user ID: {}", user.getId());
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
            logger.debug("Updating password for user ID: {}", user.getId());
            if (!userDTO.getNewPassword().equals(userDTO.getConfirmNewPassword())) {
                logger.error("Passwords do not match for user ID: {}", user.getId());
                throw new IllegalStateException("Passwords do not match");
            }
            user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        }
    }

    private void updateReceiverDetails(User user, UserDTO userDTO, MultipartFile bankDetailsImage) {
        logger.debug("Updating receiver details for user ID: {}", user.getId());
        ReceiverDetails receiverDetails = user.getReceiverDetails();
        if (receiverDetails == null) {
            logger.debug("Creating new receiver details for user ID: {}", user.getId());
            receiverDetails = new ReceiverDetails();
            receiverDetails.setUser(user);
        }

        PaymentMethod paymentMethod = PaymentMethod.valueOf(userDTO.getPaymentMethod().name());
        receiverDetails.setPaymentMethod(paymentMethod);

        if (paymentMethod == PaymentMethod.TWINT) {
            logger.debug("Setting bank details image to null for TWINT payment method.");
            receiverDetails.setBankDetailsImage(null);
        } else if (bankDetailsImage != null && !bankDetailsImage.isEmpty()) {
            logger.debug("Saving bank details image for user ID: {}", user.getId());
            String fileName = saveImageFile(bankDetailsImage);
            receiverDetails.setBankDetailsImage(fileName);
        }

        receiverDetailsRepository.save(receiverDetails);
        logger.info("Receiver details updated successfully for user ID: {}", user.getId());
    }

    private UserDTO createUserDTOWithTotals(User user) {
        logger.debug("Creating UserDTO with totals for user ID: {}", user.getId());
        Double totalDonated = user.getDonationsHistory().stream()
                .mapToDouble(Donation::getAmount)
                .sum();

        Double totalReceived = user.getZakatHistory().stream()
                .mapToDouble(Zakat::getAmountReceived)
                .sum();

        return UserDTO.fromEntity(user, totalDonated, totalReceived);
    }
}
package org.zakat.distribution.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zakat.distribution.dtos.UserDTO;
import org.zakat.distribution.entities.Donation;
import org.zakat.distribution.entities.User;
import org.zakat.distribution.entities.Zakat;
import org.zakat.distribution.exceptions.ResourceNotFoundException;
import org.zakat.distribution.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUser() {
        try {
            logger.info("Fetching profile for current user");
            User user = userService.getCurrentUser();
            Double totalDonated = user.getDonationsHistory().stream()
                    .mapToDouble(Donation::getAmount)
                    .sum();

            Double totalReceived = user.getZakatHistory().stream()
                    .mapToDouble(Zakat::getAmountReceived)
                    .sum();

            logger.info("Successfully fetched profile for current user with ID: {}", user.getId());
            return ResponseEntity.ok(UserDTO.fromEntity(user, totalDonated, totalReceived));
        } catch (Exception e) {
            logger.error("Failed to fetch user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch user profile: " + e.getMessage()));
        }
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCurrentUserProfile(
            @Valid @RequestPart("userDTO") UserDTO userDTO,
            BindingResult bindingResult,
            @RequestPart(value = "bankDetailsImage", required = false) MultipartFile bankDetailsImage
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            logger.warn("Validation failed for updating user profile: {}", errors);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Validation failed", "details", errors));
        }
        try {
            logger.info("Attempting to update profile for current user");
            userDTO.validatePasswordMatch();
            userDTO.validateReceiverFields();
            UserDTO updatedUser = userService.updateCurrentUser(userDTO, bankDetailsImage);
            logger.info("Successfully updated profile for current user with ID: {}", updatedUser.getId());
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error while updating user profile", e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Failed to update user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update user profile: " + e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        try {
            logger.info("Fetching all users");
            List<UserDTO> users = userService.getAllUsers()
                    .stream()
                    .filter(user -> !user.getRole().equals("ADMIN"))
                    .toList();
            logger.info("Successfully fetched {} users", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Failed to fetch users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch users: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long userId) {
        try {
            logger.info("Attempting to delete user with ID: {}", userId);
            userService.removeUserById(userId);
            logger.info("User deleted successfully with ID: {}", userId);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (ResourceNotFoundException e) {
            logger.error("User not found with ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Failed to delete user with ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete user: " + e.getMessage()));
        }
    }
}
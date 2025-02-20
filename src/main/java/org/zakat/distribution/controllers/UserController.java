package org.zakat.distribution.controllers;

import jakarta.validation.Valid;
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
import org.zakat.distribution.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUser() {
        try {
            User user = userService.getCurrentUser();
            Double totalDonated = user.getDonationsHistory().stream()
                    .mapToDouble(Donation::getAmount)
                    .sum();

            Double totalReceived = user.getZakatHistory().stream()
                    .mapToDouble(Zakat::getAmountReceived)
                    .sum();

            return ResponseEntity.ok(UserDTO.fromEntity(user, totalDonated, totalReceived));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch user profile: " + e.getMessage()));
        }
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCurrentUserProfile(
            @Valid @RequestPart("userDTO") UserDTO userDTO,
            BindingResult bindingResult, // Captures validation errors
            @RequestPart(value = "bankDetailsImage", required = false) MultipartFile bankDetailsImage
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Validation failed", "details", errors));
        }
        try {
            userDTO.validatePasswordMatch();
            userDTO.validateReceiverFields();
            UserDTO updatedUser = userService.updateCurrentUser(userDTO, bankDetailsImage);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update user profile: " + e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers()
                    .stream()
                    .filter(user -> !user.getRole().equals("ADMIN"))
                    .toList();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch users: " + e.getMessage()));
        }
    }
}
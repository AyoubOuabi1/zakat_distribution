package org.zakat.distribution.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.zakat.distribution.auth.JwtService;
import org.zakat.distribution.dtos.LoginDTO;
import org.zakat.distribution.dtos.LoginResponse;
import org.zakat.distribution.dtos.RegisterDTO;
import org.zakat.distribution.services.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtUtil;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        logger.info("AuthController initialized with dependencies.");
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerUser(
            @Valid @ModelAttribute RegisterDTO registerDTO,
            BindingResult bindingResult) {
        logger.info("Attempting to register user with email: {}", registerDTO.getEmail());

        if (bindingResult.hasErrors()) {
            logger.error("Validation errors found during registration for email: {}", registerDTO.getEmail());
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
                logger.debug("Validation error - Field: {}, Message: {}", error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(Map.of("error", "Validation failed", "details", errors));
        }

        try {
            logger.debug("Validating receiver fields, password match, and bank details image.");
            registerDTO.validateReceiverFields();
            registerDTO.validatePasswordMatch();
            registerDTO.validateBankDetailsImage();

            userService.registerUser(registerDTO);
            logger.info("User registered successfully with email: {}", registerDTO.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User successfully registered"));
        } catch (IllegalArgumentException e) {
            logger.error("Registration failed for email: {}. Error: {}", registerDTO.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        logger.info("Attempting to login user with email: {}", loginDTO.getEmail());

        try {
            logger.debug("Authenticating user with email: {}", loginDTO.getEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            logger.debug("User authenticated successfully: {}", userDetails.getUsername());

            String jwtToken = jwtUtil.generateToken(userDetails);
            String role = userService.getCurrentUser().getRole().toString();
            logger.info("JWT token generated for user: {}", userDetails.getUsername());

            Map<String, String> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("role", role);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            logger.error("Login failed for email: {}. Invalid credentials.", loginDTO.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid credentials", null));
        }
    }
}
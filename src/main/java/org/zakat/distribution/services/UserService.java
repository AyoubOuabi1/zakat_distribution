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

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        if (!currentUser.getEmail().equals(userDTO.getEmail()) &&
                userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        currentUser.setFullName(userDTO.getFullName());
        currentUser.setEmail(userDTO.getEmail());
        currentUser.setAddress(userDTO.getAddress());
        currentUser.setPhoneNumber(userDTO.getPhoneNumber());
        currentUser.setCanton(userDTO.getCanton());
        currentUser.setPostalCode(userDTO.getPostalCode());
        currentUser.setRole(Role.valueOf(userDTO.getRole()));
        User updatedUser = userRepository.save(currentUser);
        return UserDTO.fromEntity(updatedUser);
    }
}


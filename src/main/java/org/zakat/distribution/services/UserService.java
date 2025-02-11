package org.zakat.distribution.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zakat.distribution.dtos.RegisterDTO;
import org.zakat.distribution.dtos.UserDTO;
import org.zakat.distribution.entities.User;
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

}


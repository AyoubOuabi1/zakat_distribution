package org.zakat.distribution.controllers;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zakat.distribution.dtos.UserDTO;
import org.zakat.distribution.services.UserService;
@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUser() {
        return ResponseEntity.ok(UserDTO.fromEntity(userService.getCurrentUser()));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateCurrentUserProfile(@RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateCurrentUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }
}

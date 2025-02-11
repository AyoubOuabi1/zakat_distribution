package org.zakat.distribution.controllers;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zakat.distribution.dtos.UserDTO;
import org.zakat.distribution.services.UserService;
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<?> getUser() {
        return ResponseEntity.ok(UserDTO.fromEntity(userService.getCurrentUser()));
    }
}

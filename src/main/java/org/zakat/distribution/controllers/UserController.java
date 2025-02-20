package org.zakat.distribution.controllers;

import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zakat.distribution.dtos.UserDTO;
import org.zakat.distribution.services.UserService;

import java.util.List;
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
        return ResponseEntity.ok(UserDTO.fromEntity(userService.getCurrentUser()));
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> updateCurrentUserProfile (
            @RequestPart("userDTO") UserDTO userDTO,
            @RequestPart(value = "bankDetailsImage", required = false) MultipartFile bankDetailsImage) throws Exception{
        UserDTO updatedUser = userService.updateCurrentUser(userDTO, bankDetailsImage);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .filter(user -> !user.getRole().equals("ADMIN"))
                .collect(Collectors.toList());
    }

}

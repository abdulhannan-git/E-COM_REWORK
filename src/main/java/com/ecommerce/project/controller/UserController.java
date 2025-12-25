package com.ecommerce.project.controller;

import com.ecommerce.project.dto.UserRequest;
import com.ecommerce.project.dto.UserResponse;
import com.ecommerce.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody UserRequest userRequest) {
        userService.addUser(userRequest);
        return new ResponseEntity<>("User is created successfully.", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest userRequest
    ) {
        boolean updatedUser = userService.updateUser(id, userRequest);
        if (updatedUser)
            return new ResponseEntity<>("User details updated successfully..", HttpStatus.OK);
        return new ResponseEntity<>("User details failed to update..", HttpStatus.BAD_REQUEST);
    }
}

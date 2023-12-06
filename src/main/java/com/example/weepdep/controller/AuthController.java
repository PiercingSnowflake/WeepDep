package com.example.weepdep.controller;

import com.example.weepdep.model.LoginRequest;
import com.example.weepdep.model.SignUpRequest;
import com.example.weepdep.model.User;
import com.example.weepdep.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public User signUp(@RequestBody SignUpRequest request) {
        return userService.createUser(request.getUsername(), request.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        User user = userService.getUserByUsername(request.getUsername());

        if (user != null && user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.ok("You successfully logged in as " + user.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }



}
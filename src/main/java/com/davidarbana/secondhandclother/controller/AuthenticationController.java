package com.davidarbana.secondhandclother.controller;

import com.davidarbana.secondhandclother.model.User;
import com.davidarbana.secondhandclother.repository.UserRepository;
import com.davidarbana.secondhandclother.security.JwtService;
import com.davidarbana.secondhandclother.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Register a new user
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        // Simple check: If the user already exists, throw an exception
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        // Save the user to the database
        userService.register(user);
        return jwtService.generateToken(user.getUsername());
    }

    // Login and get JWT token
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        // Authenticate the user based on username and password
        return userService.login(user);
    }
}

package com.davidarbana.secondhandclother.service;

import com.davidarbana.secondhandclother.model.User;
import com.davidarbana.secondhandclother.repository.UserRepository;
import com.davidarbana.secondhandclother.security.JwtService;
import com.davidarbana.secondhandclother.token.Token;
import com.davidarbana.secondhandclother.token.TokenRepository;
import com.davidarbana.secondhandclother.token.TokenType;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    // Register a new user
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // Login user and return JWT token
    public String login(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        String generatedToken = jwtService.generateToken(user.getUsername());
        revokeAllUserTokens(existingUser);
        var token = Token.builder()
                .user(existingUser)
                .token(generatedToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}

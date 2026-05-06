package com.example.backend.service;

import com.example.backend.dto.auth.AuthResponse;
import com.example.backend.dto.auth.LoginRequest;
import com.example.backend.dto.auth.RegisterRequest;
import com.example.backend.entity.User;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.UnauthorizedException;
import com.example.backend.security.CookieConfig;
import com.example.backend.security.JwtUtil;
import com.example.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CookieConfig cookieConfig;

    public AuthResponse register(RegisterRequest req, HttpServletResponse res) {
        if (userService.existsByEmail(req.getEmail()))
            throw new IllegalArgumentException("Email already in use");

        User user = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .build();

        userRepository.save(user);
        setAuthCookie(user, res);
        return new AuthResponse(user);
    }

    public AuthResponse login(LoginRequest req, HttpServletResponse res) throws UnauthorizedException {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash()))
            throw new UnauthorizedException("Invalid credentials");

        setAuthCookie(user, res);
        return new AuthResponse(user);
    }

    public void logout(HttpServletResponse res) {
        res.addCookie(cookieConfig.clearJwtCookie());
    }

    public AuthResponse me(Long userId) throws ResourceNotFoundException {
        User user = userService.findById(userId);
        return new AuthResponse(user);
    }

    private void setAuthCookie(User user, HttpServletResponse res) {
        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        res.addCookie(cookieConfig.buildJwtCookie(token));
    }
}

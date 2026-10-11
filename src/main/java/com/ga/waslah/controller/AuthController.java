package com.ga.waslah.controller;

import com.ga.waslah.dto.LoginRequest;
import com.ga.waslah.dto.LoginResponse;
import com.ga.waslah.exception.BadRequestException;
import com.ga.waslah.exception.ForbiddenException;
import com.ga.waslah.exception.ResourceNotFoundException;
import com.ga.waslah.model.User;
import com.ga.waslah.repository.UserRepository;
import com.ga.waslah.security.JwtService;
import com.ga.waslah.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest loginRequest
    ) {

        User user = userRepository.findByUsername(
                loginRequest.getUsername()
        ).orElseThrow(() ->
                new BadRequestException(
                        "Invalid username or password"
                )
        );

        if (user.getStatus() !=
                com.ga.waslah.model.UserStatus.ACTIVE) {

            throw new ForbiddenException(
                    "Your account is inactive and cannot be used"
            );
        }

        if (!user.isEmailVerified()) {
            throw new ForbiddenException(
                    "Please verify your email before logging in"
            );
        }

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );

        String token = jwtService.generateToken(
                authentication.getName()
        );

        return new LoginResponse(token);
    }

    @GetMapping("/verify-email")
    public String verifyEmail(
            @RequestParam String token
    ) {

        emailVerificationService.verifyEmail(token);

        return "Email verified successfully. You can now login.";
    }
}

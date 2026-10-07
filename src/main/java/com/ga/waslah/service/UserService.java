package com.ga.waslah.service;

import com.ga.waslah.dto.ChangePasswordRequest;
import com.ga.waslah.dto.RegisterRequest;
import com.ga.waslah.exception.BadRequestException;
import com.ga.waslah.exception.ConflictException;
import com.ga.waslah.exception.ResourceNotFoundException;
import com.ga.waslah.model.Role;
import com.ga.waslah.model.User;
import com.ga.waslah.model.UserStatus;
import com.ga.waslah.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // Users always start with the USER role.
        user.setRole(Role.USER);

        // New users are active by default.
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    public void deactivateUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with ID " + userId + " was not found"
                        )
                );

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new ConflictException(
                    "User is already inactive"
            );
        }

        user.setStatus(UserStatus.INACTIVE);

        userRepository.save(user);
    }


    public void changePassword(
            String username,
            ChangePasswordRequest request
    ) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        // Verify the current password before allowing the change.
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword()
        )) {
            throw new BadRequestException(
                    "Current password is incorrect"
            );
        }

        // Prevent the user from using the same password again.
        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword()
        )) {
            throw new BadRequestException(
                    "New password must be different from the current password"
            );
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);
    }

}
package com.ga.waslah.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfileRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Size(max = 8, message = "Phone number must not exceed 8 digits")
    private String phone;

    @Size(max = 450, message = "Bio must not exceed 450 characters")
    private String bio;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;
}
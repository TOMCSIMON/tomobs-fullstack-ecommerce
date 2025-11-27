package com.tomobs.ecommerce.dto;

// DTO COLLECTS AND VALIDATES USER INPUT FROM THE SIGNUP FORM BEFORE CONVERTING IT INTO THE USER ENTITY
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {

    @NotBlank(message = "username cannot be empty!")
    @Size(min = 3, max = 20, message = "username must be between 3 to 20 Characters")
    private String userName;

    @NotBlank(message = "Email can not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 17, message = "Phone number must be between 10 and 17 digits/characters")
    private String phoneNumber;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, one special character, and no whitespace, and be at least 8 characters long")
    private String password;

    @NotBlank(message = "Confirm password cannot be empty")
    private String confirmPassword;
}

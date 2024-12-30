package nl.casperdaris.boekenkast.authentication;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// TODO: Add documentation
@Getter
@Setter
@Builder
public class RegisterUserRequest {

    @NotEmpty(message = "Username is required")
    @NotBlank(message = "Username is required")
    private String username;

    // @NotEmpty(message = "Date of birth is required")
    // @NotBlank(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @Email(message = "Email is invalid")
    @NotEmpty(message = "Email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
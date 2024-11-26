package oBeta.PiggyWebBank.payloads.admin;

import jakarta.validation.constraints.*;

public record UserDTO(
        String name,
        String surname,
        @NotEmpty(message = "Username must be set!")
        String username,
        @NotEmpty(message = "Email must be set!")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "The email address you entered is invalid!")
        String email,
        @NotEmpty(message = "Password must be set!")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]{8,}$",
                message = "The password is not secure enough!"
        )
        String password,
        @NotNull(message = "Role Id must be set!")
        long role_id
) {
}

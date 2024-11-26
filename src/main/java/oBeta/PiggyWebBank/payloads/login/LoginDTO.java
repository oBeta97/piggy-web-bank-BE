package oBeta.PiggyWebBank.payloads.login;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record LoginDTO(
        @NotEmpty(message = "Username must be set!")
        String username,
        @NotEmpty(message = "Password must be set!")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]{8,}$",
                message = "The password is not secure enough!"
        )
        String password
) {
}

package oBeta.PiggyWebBank.payloads.me;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import oBeta.PiggyWebBank.regex.Regex;

public record UpdatePasswordDTO(
        @NotEmpty(message = "Password must be set!")
        @Pattern(
                regexp = Regex.PASSWORD,
                message = "The password is not secure enough!"
        )
        String password
) {
}

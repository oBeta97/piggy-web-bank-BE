package oBeta.PiggyWebBank.payloads.signin;

import jakarta.persistence.Column;

public record SigninResponseDTO(
        String name,
        String surname,
        String username,
        String email
) {
}

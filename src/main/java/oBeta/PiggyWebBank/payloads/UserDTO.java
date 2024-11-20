package oBeta.PiggyWebBank.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Setter;
import oBeta.PiggyWebBank.entities.Role;

import java.time.LocalDate;

public record UserDTO(
        String name,
        String surname,
        @NotEmpty(message = "Username must be set!")
        String username,
        @NotEmpty(message = "Email must be set!")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\n", message = "The email address you entered is invalid!")
        String email,
        @NotEmpty(message = "Password must be set!")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$\n", message = "The password is not secure enough!")
        String password,
        @PastOrPresent(message = "The last password update can't be in the future!")
        LocalDate lastPasswordUpdate,
        @NotNull(message = "Role Id must be set!")
        long role_id
) {
}

package oBeta.PiggyWebBank.payloads;

import java.time.LocalDateTime;

public record ErrorsResponseDTO(
        String errorCode,
        String message,
        LocalDateTime dt) {
}

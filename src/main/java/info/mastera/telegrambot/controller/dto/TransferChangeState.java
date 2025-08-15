package info.mastera.telegrambot.controller.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;


public record TransferChangeState(
        @NotBlank String regNum,
        @NonNull StateChangeType changeType,
        @NonNull Status previousStatus,
        @NonNull Status actualStatus,
        @NonNull Integer previousOrderId,
        @NonNull Integer actualOrderId
) {
}

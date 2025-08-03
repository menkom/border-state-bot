package info.mastera.telegrambot.controller.dto;

import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;

public record TransferChangeState(
        @NotBlank String regNum,
        @NonNull StateChangeType changeType,
        @NonNull Status previousStatus,
        @NonNull Status actualStatus,
        @NonNull Integer previousOrderId,
        @NonNull Integer actualOrderId
) {
}

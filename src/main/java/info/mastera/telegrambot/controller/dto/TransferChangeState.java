package info.mastera.telegrambot.controller.dto;

import org.springframework.lang.NonNull;

public record TransferChangeState(
        @NonNull StateChangeType changeType,
        @NonNull Status previousStatus,
        @NonNull Status actualStatus,
        @NonNull Integer previousOrderId,
        @NonNull Integer actualOrderId
) {
}

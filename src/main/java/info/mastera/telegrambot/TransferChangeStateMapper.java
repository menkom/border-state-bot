package info.mastera.telegrambot;

import info.mastera.telegrambot.controller.dto.Status;
import info.mastera.telegrambot.controller.dto.TransferChangeState;
import org.springframework.stereotype.Service;

@Service
public class TransferChangeStateMapper {

    public String convert(TransferChangeState transferChangeState) {
        return switch (transferChangeState.changeType()) {
            case NEW ->
                    "Транспорт с номером %s заехал в зону ожидания и находится %s в очереди".formatted(transferChangeState.regNum(),
                            transferChangeState.actualOrderId());
            case ORDER_ID ->
                    "Транспорт с номером %s сменил номер в очереди с %s на %s".formatted(transferChangeState.regNum(),
                            transferChangeState.previousOrderId(), transferChangeState.actualOrderId());
            case STATUS -> "Транспорт с номером %s сменил статус с %s на %s".formatted(transferChangeState.regNum(),
                    convert(transferChangeState.previousStatus()), convert(transferChangeState.actualStatus()));
            default ->
                    throw new IllegalArgumentException("Change type %s is unexpected".formatted(transferChangeState.changeType()));
        };
    }

    public String convert(Status status) {
        return switch (status) {
            case ARRIVED -> "ПРИБЫЛ";
            case CALLED -> "ВЫЗВАН К ГРАНИЦЕ";
            case ANNULLED -> "АННУЛИРОВАН";
            default -> throw new IllegalArgumentException("Status %s is unexpected".formatted(status));
        };
    }
}

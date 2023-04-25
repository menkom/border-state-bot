package info.mastera.telegrambot.command;

import info.mastera.telegrambot.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class SubscribeCommand extends BotCommand {

    private final SubscriptionService subscriptionService;

    public SubscribeCommand(SubscriptionService subscriptionService) {
        super("subscribe", "Укажите номер транспорта, уведомления об изменениях статуса которого вы хотели бы получать. Пример: /subscribe 1234AA5");
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            if (validArguments(absSender, chat.getId().toString(), arguments)) {
                subscriptionService.save(chat.getId(), arguments[0]);
                sendMessage(absSender, chat.getId().toString(), "Вы будете получать сообщения статуса для автомобильного номера %s".formatted(arguments[0]));
            }
        } catch (TelegramApiException e) {
            log.error(this.getClass().getSimpleName(), e);
        }
    }

    private void sendMessage(AbsSender absSender, String chatId, String message) throws TelegramApiException {
        absSender.execute(new SendMessage(chatId, message));
    }

    private boolean validArguments(AbsSender absSender, String chatId, String[] arguments) throws TelegramApiException {
        if (arguments == null) {
            sendMessage(
                    absSender,
                    chatId,
                    "Необходимо указать номер траспорта для отслеживания. Пример: %s 1234AA5".formatted(getCommandIdentifier())
            );
            return false;
        }
        if (arguments.length != 1) {
            sendMessage(
                    absSender,
                    chatId,
                    "Разрешается только один автомобильный номер для отслеживания."
            );
            return false;
        }
        return true;
    }
}

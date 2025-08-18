package info.mastera.telegrambot.command;

import info.mastera.telegrambot.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Arrays;

@Slf4j
@Component
public class SubscribeCommand extends BotCommand {

    private final SubscriptionService subscriptionService;

    public SubscribeCommand(SubscriptionService subscriptionService) {
        super("subscribe", "Укажите номер транспорта, уведомления об изменениях статуса которого вы хотели бы получать. Пример: /subscribe 1234AA5");
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, String[] arguments) {
        try {
            log.info("Chat {} trying to subscribe to {}.", chat.getId(), Arrays.toString(arguments));
            if (validArguments(telegramClient, chat.getId().toString(), arguments)) {
                subscriptionService.save(chat.getId(), arguments[0]);
                sendMessage(telegramClient, chat.getId().toString(), "Вы будете получать сообщения статуса для автомобильного номера %s".formatted(arguments[0]));
                log.info("Chat {} subscribed to {}.", chat.getId(), arguments[0]);
            }
        } catch (TelegramApiException e) {
            log.error(this.getClass().getSimpleName(), e);
        }
    }

    private void sendMessage(TelegramClient telegramClient, String chatId, String message) throws TelegramApiException {
        telegramClient.execute(new SendMessage(chatId, message));
    }

    private boolean validArguments(TelegramClient telegramClient, String chatId, String[] arguments) throws TelegramApiException {
        if (arguments == null) {
            sendMessage(
                    telegramClient,
                    chatId,
                    "Необходимо указать номер транспорта для отслеживания. Пример: %s 1234AA5".formatted(getCommandIdentifier())
            );
            return false;
        }
        if (arguments.length == 0) {
            sendMessage(
                    telegramClient,
                    chatId,
                    "К команде необходимо добавить номер автомобиля."
            );
            return false;
        } else if (arguments.length > 1) {
            sendMessage(
                    telegramClient,
                    chatId,
                    "Разрешается только один автомобильный номер для отслеживания."
            );
            return false;
        }
        return true;
    }
}

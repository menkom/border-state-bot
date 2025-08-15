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

@Slf4j
@Component
public class ClearCommand extends BotCommand {

    private final SubscriptionService subscriptionService;

    public ClearCommand(SubscriptionService subscriptionService) {
        super("clear", "Приостановить отправку уведомлений");
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, String[] arguments) {
        try {
            subscriptionService.deleteByChatId(chat.getId().toString());
            SendMessage answer = new SendMessage(chat.getId().toString(), "Отправка уведомлений приостановлена");
            telegramClient.execute(answer);
        } catch (TelegramApiException e) {
            log.error(this.getClass().getSimpleName(), e);
        }
    }
}
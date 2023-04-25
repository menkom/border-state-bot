package info.mastera.telegrambot.command;

import info.mastera.telegrambot.repository.SubscriptionRepository;
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
public class ClearCommand extends BotCommand {

    private final SubscriptionRepository subscriptionRepository;

    public ClearCommand(SubscriptionRepository subscriptionRepository) {
        super("clear", "Приостановить отправку уведомлений");
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            SendMessage answer;
            if (subscriptionRepository.deleteByChatId(chat.getId().toString())) {
                answer = new SendMessage(chat.getId().toString(), "Отправка уведомлений приостановлена");
            } else {
                answer = new SendMessage(chat.getId().toString(), "Произошла ошибка. Повторите команду еще раз");
            }
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error(this.getClass().getSimpleName(), e);
        }
    }
}
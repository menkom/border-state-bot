package info.mastera.telegrambot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.validation.constraints.NotBlank;

@Slf4j
@Component
public class TransmitterBot extends TelegramLongPollingBot {

    private final String botUsername;

    public TransmitterBot(TelegramSettings telegramSettings) {
        super(telegramSettings.getToken());
        getOptions().setGetUpdatesTimeout(Integer.MAX_VALUE);
        this.botUsername = telegramSettings.getUsername();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Update:" + update);
        try {
            Message message = update.getMessage();
            execute(new SendMessage(message.getChatId().toString(), "Bot received this message:" + message.getText()));
        } catch (TelegramApiException e) {
            log.error("Error sending message to user.", e);
        }
    }

    @Retryable(
            value = {TelegramApiException.class, TelegramApiRequestException.class},
            maxAttemptsExpression = "${telegram.send-retries}",
            backoff = @Backoff(delayExpression = "${telegram.send-retry-delay}")
    )
    public void sendMessageToChat(@NotBlank String text) throws TelegramApiException {
//        var message = new SendMessage(chatId, text);
//        execute(message);
    }
}

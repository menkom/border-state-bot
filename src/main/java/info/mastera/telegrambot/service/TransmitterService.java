package info.mastera.telegrambot.service;

import info.mastera.telegrambot.TransferChangeStateMapper;
import info.mastera.telegrambot.controller.dto.TransferChangeState;
import info.mastera.telegrambot.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransmitterService {

    private final AbsSender botSender;
    private final TransferChangeStateMapper transferChangeStateMapper;
    private final SubscriptionRepository subscriptionRepository;

    @Retryable(
            value = {TelegramApiException.class, TelegramApiRequestException.class},
            maxAttemptsExpression = "${telegram.send-retries}",
            backoff = @Backoff(delayExpression = "${telegram.send-retry-delay}")
    )
    public void transmitMessage(TransferChangeState transferChangeState) {
        subscriptionRepository.findByRegNum(transferChangeState.regNum().toUpperCase())
                .forEach(subscription -> sendInfo(subscription.getChatId(), transferChangeState));
    }

    private void sendInfo(String chatId, TransferChangeState transferChangeState) {
        try {
            botSender.execute(new SendMessage(chatId, transferChangeStateMapper.convert(transferChangeState)));
        } catch (TelegramApiException e) {
            log.error("Error on user informing chatId %s with regNum %s".formatted(chatId, transferChangeState.regNum()), e);
        }
    }
}

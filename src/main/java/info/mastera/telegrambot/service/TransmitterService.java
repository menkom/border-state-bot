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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransmitterService {

    private final TelegramClient telegramClient;
    private final TransferChangeStateMapper transferChangeStateMapper;
    private final SubscriptionRepository subscriptionRepository;

    @Retryable(
            retryFor = {TelegramApiException.class, TelegramApiRequestException.class},
            maxAttemptsExpression = "${telegram.send-retries}",
            backoff = @Backoff(delayExpression = "${telegram.send-retry-delay}")
    )
    public void transmitMessage(TransferChangeState transferChangeState) {
        subscriptionRepository.findByRegNum(transferChangeState.regNum().toUpperCase())
                .forEach(subscription -> sendInfo(subscription.getChatId(), transferChangeState));
    }

    private void sendInfo(String chatId, TransferChangeState transferChangeState) {
        try {
            telegramClient.execute(new SendMessage(chatId, transferChangeStateMapper.convert(transferChangeState)));
        } catch (TelegramApiException e) {
            log.error("Error on user informing chatId {} with regNum {}", chatId, transferChangeState.regNum(), e);
        }
    }
}

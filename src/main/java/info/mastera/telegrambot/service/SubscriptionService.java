package info.mastera.telegrambot.service;

import info.mastera.telegrambot.TransferChangeStateMapper;
import info.mastera.telegrambot.bot.BorderStateBot;
import info.mastera.telegrambot.controller.dto.TransferChangeState;
import info.mastera.telegrambot.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final BorderStateBot borderStateBot;
    private final TransferChangeStateMapper transferChangeStateMapper;

    public void transmitMessage(TransferChangeState transferChangeState) {
        subscriptionRepository.findByRegNum(transferChangeState.regNum().toUpperCase())
                .forEach(subscription -> sendInfo(subscription.getChatId(), transferChangeState));
    }

    private void sendInfo(String chatId, TransferChangeState transferChangeState) {
        try {
            borderStateBot.sendMessageToChat(chatId, transferChangeStateMapper.convert(transferChangeState));
        } catch (TelegramApiException e) {
            log.error("Error on user informing chatId %s with regNum %s".formatted(chatId, transferChangeState.regNum()), e);
        }
    }
}

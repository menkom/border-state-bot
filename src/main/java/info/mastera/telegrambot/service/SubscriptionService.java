package info.mastera.telegrambot.service;

import info.mastera.telegrambot.converter.CyrillicConverter;
import info.mastera.telegrambot.model.Subscription;
import info.mastera.telegrambot.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CyrillicConverter cyrillicConverter;

    @Transactional
    public void deleteByChatId(String chatId) {
        subscriptionRepository.deleteByChatId(chatId);
    }

    public void save(Long chatId, String argument) {
        subscriptionRepository.save(getSubscription(chatId, argument));
    }

    private Subscription getSubscription(Long chatId, String argument) {
        var subscription = subscriptionRepository.findByChatId(chatId.toString());
        return new Subscription()
                .setId(subscription == null ? null : subscription.getId())
                .setChatId(chatId.toString())
                .setRegNum(cyrillicConverter.convertWord(argument).toUpperCase());
    }
}

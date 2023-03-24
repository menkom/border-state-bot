package info.mastera.telegrambot.repository;

import info.mastera.telegrambot.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Subscription findByChatId(String chatId);

    List<Subscription> findByRegNum(String regNum);
}

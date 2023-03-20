package info.mastera.telegrambot;

import info.mastera.telegrambot.bot.TelegramSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableRetry
@EnableConfigurationProperties(value = {TelegramSettings.class})
@SpringBootApplication
public class BorderStateTelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(BorderStateTelegramBotApplication.class, args);
    }
}

package info.mastera.telegrambot.bot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramConfig {

    @Bean
    public TelegramClient telegramClient(TelegramSettings telegramSettings) {
        return new OkHttpTelegramClient(telegramSettings.getToken());
    }

    @Bean
    public CommandRegistry commandRegistry(TelegramClient telegramClient,
                                           TelegramSettings telegramSettings) {
        return new CommandRegistry(telegramClient, true,
                telegramSettings::getUsername);
    }
}

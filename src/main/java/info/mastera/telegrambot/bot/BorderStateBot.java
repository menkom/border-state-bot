package info.mastera.telegrambot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Slf4j
@Component
public class BorderStateBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final TelegramSettings telegramSettings;
    private final CommandRegistry commandRegistry;

    public BorderStateBot(TelegramClient telegramClient,
                          TelegramSettings telegramSettings,
                          List<IBotCommand> commands) throws TelegramApiException {
        this.telegramClient = telegramClient;
        this.telegramSettings = telegramSettings;
        this.commandRegistry = new CommandRegistry(telegramClient, true, telegramSettings::getUsername);
        registerCommands(commands);
    }

    @Override
    public String getBotToken() {
        return telegramSettings.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            var message = update.getMessage();
            if (message.isCommand()) {
                if (!commandRegistry.executeCommand(message)) {
                    processInvalidCommandUpdate(update);
                }
            } else {
                processNonCommandUpdate(update);
            }
        }
        processNonCommandUpdate(update);
    }

    private void processInvalidCommandUpdate(Update update) {
        try {
            var message = update.getMessage();
            if (message != null) {
                telegramClient.execute(new SendMessage(message.getChatId().toString(), "Неправильная команда %s".formatted(message.getText())));
            }
        } catch (TelegramApiException e) {
            log.error("Error sending message to user.", e);
        }
    }

    private void processNonCommandUpdate(Update update) {
        try {
            var message = update.getMessage();
            if (message != null) {
                telegramClient.execute(new SendMessage(message.getChatId().toString(), "Команда %s не существует".formatted(message.getText())));
            }
        } catch (TelegramApiException e) {
            log.error("Error sending message to user.", e);
        }
    }

    private void registerCommands(List<IBotCommand> commands) throws TelegramApiException {
        commands.forEach(commandRegistry::register);
        var botCommands = commands.stream()
                .map(command -> new BotCommand(command.getCommandIdentifier(), command.getDescription()))
                .toList();
        telegramClient.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), "ru"));
    }
}

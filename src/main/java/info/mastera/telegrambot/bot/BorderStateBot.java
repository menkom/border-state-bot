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
import org.telegram.telegrambots.meta.api.objects.EntityType;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class BorderStateBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final TelegramSettings telegramSettings;
    private final CommandRegistry commandRegistry;

    public BorderStateBot(TelegramClient telegramClient,
                          CommandRegistry commandRegistry,
                          TelegramSettings telegramSettings,
                          List<IBotCommand> commands) throws TelegramApiException {
        this.telegramClient = telegramClient;
        this.telegramSettings = telegramSettings;
        this.commandRegistry = commandRegistry;
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
            if (isCommand(message)) {
                if (!commandRegistry.executeCommand(message)) {
                    processInvalidCommandUpdate(update);
                }
            } else {
                processNonCommandUpdate(update);
            }
        } else {
            processNonCommandUpdate(update);
        }
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

    /**
     * There is message.isCommand() method but Telegram-server recognized message
     * as text_link but not bot_command then we can't process it correctly.
     * So we have to override method isCommand.
     */
    private boolean isCommand(Message message) {
        Objects.requireNonNull(message, "Message cannot be null");
        if (message.hasText() && message.getEntities() != null) {
            for (MessageEntity entity : message.getEntities()) {
                if (entity != null && entity.getOffset() == 0 &&
                        (EntityType.BOTCOMMAND.equals(entity.getType())
                                || (EntityType.TEXTLINK.equals(entity.getType())
                                && isCommand(entity.getText())))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCommand(String entity) {
        return entity.startsWith("/")
                && commandRegistry.getRegisteredCommands().stream()
                .map(IBotCommand::getCommandIdentifier)
                .anyMatch(command -> command.equals(entity.replaceFirst("/", "")));
    }
}

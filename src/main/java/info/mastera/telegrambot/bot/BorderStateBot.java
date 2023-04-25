package info.mastera.telegrambot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.CommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Component
public class BorderStateBot extends TelegramLongPollingBot implements CommandBot {

    private final String botUsername;

    private final CommandRegistry commandRegistry;

    public BorderStateBot(TelegramSettings telegramSettings, List<IBotCommand> commands) throws TelegramApiException {
        super(telegramSettings.getToken());
        this.botUsername = telegramSettings.getUsername();
        this.commandRegistry = new CommandRegistry(true, this::getBotUsername);
        registerCommands(commands);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.isCommand() && !filter(message)) {
                if (!commandRegistry.executeCommand(this, message)) {
                    processInvalidCommandUpdate(update);
                }
                return;
            }
        }
        processNonCommandUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        try {
            var message = update.getMessage();
            if (message != null) {
                execute(new SendMessage(message.getChatId().toString(), "Команда %s не существует".formatted(message.getText())));
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
        execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), "ru"));
    }
}

package info.mastera.telegrambot.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.stream.Collectors;

@Slf4j
@Component
public class StartCommand extends BotCommand {

    private final CommandRegistry commandRegistry;

    public StartCommand(CommandRegistry commandRegistry) {
        super("start", "Старт бота.");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(TelegramClient telegramClient, User user, Chat chat, String[] arguments) {
        try {
            String information = commandRegistry.getRegisteredCommands().stream()
                    .filter(command -> !"start".equals(command.getCommandIdentifier()))
                    .map(command -> command.getCommandIdentifier() + " - " + command.getDescription())
                    .collect(Collectors.joining("\n\n"));
            SendMessage informationMessage = new SendMessage(chat.getId().toString(),
                    "Для пользования ботом вы можете ввести следующие команды:\n\n"+ information);
            telegramClient.execute(informationMessage);
        } catch (TelegramApiException e) {
            log.error(this.getClass().getSimpleName(), e);
        }
    }
}

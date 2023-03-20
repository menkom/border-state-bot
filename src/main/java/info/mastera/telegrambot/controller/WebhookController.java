package info.mastera.telegrambot.controller;

import info.mastera.telegrambot.bot.TransmitterBot;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RestController
@RequiredArgsConstructor
public class WebhookController {

    private final TransmitterBot transmitterBot;

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public void transmit(@RequestBody String text) throws TelegramApiException {
        transmitterBot.sendMessageToChat(text);
    }
}

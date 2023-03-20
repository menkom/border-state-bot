package info.mastera.telegrambot.controller;

import info.mastera.telegrambot.controller.dto.TransferChangeState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebhookController {

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public void transmit(@RequestBody @Valid TransferChangeState state) {
        log.info(state.changeType().name());
    }
}

package info.mastera.telegrambot.bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

@Setter
@Getter
@Validated
@ConfigurationProperties(value = "telegram")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramSettings {

    @Pattern(regexp = "^.+(?i)(bot)$")
    String username;

    @Pattern(regexp = "\\d+:.+")
    String token;

}

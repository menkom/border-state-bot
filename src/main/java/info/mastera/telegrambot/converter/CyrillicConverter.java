package info.mastera.telegrambot.converter;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CyrillicConverter {

    private static final Map<String, String> vocabulary = Map.ofEntries(
            Map.entry("а", "a"),
            Map.entry("А", "A"),
            Map.entry("в", "b"),
            Map.entry("В", "B"),
            Map.entry("е", "e"),
            Map.entry("Е", "E"),
            Map.entry("к", "k"),
            Map.entry("К", "K"),
            Map.entry("м", "m"),
            Map.entry("М", "M"),
            Map.entry("н", "h"),
            Map.entry("Н", "H"),
            Map.entry("о", "o"),
            Map.entry("О", "O"),
            Map.entry("р", "p"),
            Map.entry("Р", "P"),
            Map.entry("с", "c"),
            Map.entry("С", "C"),
            Map.entry("т", "t"),
            Map.entry("Т", "T"),
            Map.entry("у", "y"),
            Map.entry("У", "Y"),
            Map.entry("х", "x"),
            Map.entry("Х", "X"),
            Map.entry("і", "i"),
            Map.entry("І", "I")
    );

    public String convertCharacter(String character) {
        if (character == null || character.length() != 1) {
            throw new IllegalArgumentException("Incorrect method use. Only one character is accepted.");
        }
        return vocabulary.getOrDefault(character, character);
    }

    public String convertWord(String word) {
        if (word == null) {
            return null;
        }
        return Arrays.stream(word.split(""))
                .map(this::convertCharacter)
                .collect(Collectors.joining());
    }
}

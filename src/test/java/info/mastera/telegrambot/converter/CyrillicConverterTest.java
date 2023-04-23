package info.mastera.telegrambot.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class CyrillicConverterTest {

    private final CyrillicConverter cyrillicConverter = new CyrillicConverter();

    @Nested
    public class ConvertCharacterTest {

        @ParameterizedTest
        @ValueSource(strings = {
                "а", "А",
                "в", "В",
                "е", "Е",
                "к", "К",
                "м", "М",
                "н", "Н",
                "о", "О",
                "р", "Р",
                "с", "С",
                "т", "Т",
                "у", "У",
                "х", "Х",
        })
        public void convertsToCorrespondingLetterTest(String character) {
            Assertions.assertThat(cyrillicConverter.convertCharacter(character))
                    .isNotEqualTo(character);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "б", "Б",
                "г", "Г",
                "д", "Д",
                "ё", "Ё",
                "ж", "Ж",
                "з", "З",
                "и", "И",
                "й", "Й",
                "л", "Л",
                "п", "П",
                "ф", "Ф",
                "ц", "Ц",
                "ч", "Ч",
                "ш", "Ш",
                "щ", "Щ",
                "ъ", "Ъ",
                "ы", "Ы",
                "ь", "Ь",
                "э", "Э",
                "ю", "Ю",
                "я", "Я",
                "ś", "Ś",
                "ć", "Ć",
        })
        public void convertsToSameAsNoCorrespondingLetterTest(String character) {
            Assertions.assertThat(cyrillicConverter.convertCharacter(character))
                    .isEqualTo(character);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "arg",
                ""
        })
        public void incorrectArgumentExceptionTest(String arg) {
            Assertions.assertThatThrownBy(() -> cyrillicConverter.convertCharacter(arg))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullSource
        public void nullArgumentExceptionTest(String arg) {
            Assertions.assertThatThrownBy(() -> cyrillicConverter.convertCharacter(arg))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    public class ConvertWordTest {

        @ParameterizedTest
        @CsvSource(value =  {
                "ахСмно2й, axCmho2й",
        })
        public void convertsNecessaryLettersInWordTest(String source, String expected) {
            Assertions.assertThat(cyrillicConverter.convertWord(source))
                    .isEqualTo(expected);
        }

        @Test
        public void convertNullToNullTest() {
            Assertions.assertThat(cyrillicConverter.convertWord(null))
                    .isNull();
        }
    }
}
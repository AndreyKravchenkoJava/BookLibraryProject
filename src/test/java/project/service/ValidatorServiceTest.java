package project.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class ValidatorServiceTest {

    @ParameterizedTest(name = "Test should successfully valid input add book only one time by example 'My life, my achievements / Henry Ford'")
    @ValueSource(strings = {"", "My life, my achievements Henry Ford", "My life, my achievements/ Henry Ford", "My life, my achievements / Henry Ford"})
    void shouldSuccessfullyValidInputAddBookOnlyOneTime(String input) {
        assertThat(ValidatorService.isInputAddBookValid(input)).isTrue();
    }

    @ParameterizedTest(name = "Test should successfully valid input add reader only one time by example 'Alexander Singeev'")
    @ValueSource(strings = {"", "Alexander Singeev"})
    void shouldSuccessfullyValidInputAddReaderOnlyOneTime(String input) {
        assertThat(ValidatorService.isInputAddReaderValid(input)).isTrue();
    }

    @ParameterizedTest(name = "Test should successfully valid input name or author only one time by example 'Alexander Singeev'")
    @ValueSource(strings = {"", "A", "23232", "Al3xander Singeev", "Alexander Singeev"})
    void shouldSuccessfullyValidNameReaderOrAuthorOnlyOneTime(String input) {
        assertThat(ValidatorService.isNameReaderOrAuthorValid(input)).isTrue();
    }

    @ParameterizedTest(name = "Test should successfully valid input one Id one time by example '5'")
    @ValueSource(strings = {"", "a", "a5", "5a", "5"})
    void shouldSuccessfullyValidOneIdOnlyOneTime(String input) {
        assertThat(ValidatorService.isOneIdValid(input)).isTrue();
    }

    @ParameterizedTest(name = "Test should successfully valid input two Id only one time by example '5 / 5'")
    @ValueSource(strings = {"", "a  b", "a / b", "a / 5", "5 / a", "5 5", "5/ 5", "5 / 5"})
    void shouldSuccessfullyValidTwoIdOnlyOneTime(String input) {
        assertThat(ValidatorService.isTwoIdValid(input)).isTrue();
    }
}
package project.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import project.exception.ValidatorServiceException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorServiceTest {

    @ParameterizedTest(name = "Test should successfully valid input add book or throw exception")
    @CsvSource(value = {
            ", false, input cannot be empty!",
            "'My life, my achievements Henry Ford', false, input should be separated \' / \'!",
            "'My life, my achievements/ Henry Ford', false, input should be separated \' / \'!",
            "'My life, my achievements / Henry Ford', true,"})
    void shouldSuccessfullyValidInputAddBook(String input, boolean isValid, String errorMessage) {
        if (isValid) {
            assertThat(ValidatorService.isInputAddBookValid(input)).isTrue();
        } else {
            var exception = assertThrows(ValidatorServiceException.class,
                    () -> ValidatorService.isInputAddBookValid(input));
            assertThat(errorMessage).isEqualTo(exception.getLocalizedMessage());
        }
    }

    @ParameterizedTest(name = "Test should successfully valid input add reader by example 'Alexander Singeev' or throw exception")
    @CsvSource(value = {
            ", false, input cannot be empty!",
            "'Alexander Singeev', true,"})
    void shouldSuccessfullyValidInputAddReader(String input, boolean isValid, String errorMessage) {
        if (isValid) {
            assertThat(ValidatorService.isInputAddReaderValid(input)).isTrue();
        } else {
            var exception = assertThrows(ValidatorServiceException.class,
                    () -> ValidatorService.isInputAddReaderValid(input));
            assertThat(errorMessage).isEqualTo(exception.getLocalizedMessage());
        }
    }

    @ParameterizedTest(name = "Test should successfully valid input name or author by example 'Alexander Singeev' or throw exception")
    @CsvSource(value = {
            "'', false, invalid name! 1.Name must contain only letters 2.Have more than two letters 3.You must write the name as in the example 'Danil Zanuk'",
            "'Y', false, invalid name! 1.Name must contain only letters 2.Have more than two letters 3.You must write the name as in the example 'Danil Zanuk'",
            "'333', false, invalid name! 1.Name must contain only letters 2.Have more than two letters 3.You must write the name as in the example 'Danil Zanuk'",
            "'Al3xander Singeev', false, invalid name! 1.Name must contain only letters 2.Have more than two letters 3.You must write the name as in the example 'Danil Zanuk'",
            "'Alexander Singeev', true,"})
    void shouldSuccessfullyValidNameReaderOrAuthor(String input, boolean isValid, String errorMessage) {
        if (isValid) {
            assertThat(ValidatorService.isNameReaderOrAuthorValid(input)).isTrue();
        } else {
            var exception = assertThrows(ValidatorServiceException.class,
                    () -> ValidatorService.isNameReaderOrAuthorValid(input));
            assertThat(errorMessage).isEqualTo(exception.getLocalizedMessage());
        }
    }

    @ParameterizedTest(name = "Test should successfully valid input one Id by example '5' or throw exception")
    @CsvSource(value = {
            "'', false, invalid input! 1.The input cannot be empty 2.You must enter only number 3.You must write the input as in the example '5'",
            "'a', false, invalid input! 1.The input cannot be empty 2.You must enter only number 3.You must write the input as in the example '5'",
            "'5', true,"})
    void shouldSuccessfullyValidOneId(String input, boolean isValid, String errorMessage) {
        if (isValid) {
            assertThat(ValidatorService.isOneIdValid(input)).isTrue();
        } else {
            var exception = assertThrows(ValidatorServiceException.class,
                    () -> ValidatorService.isOneIdValid(input));
            assertThat(errorMessage).isEqualTo(exception.getLocalizedMessage());
        }
    }

    @ParameterizedTest(name = "Test should successfully valid input two Id by example '5 / 5' or throw exception")
    @CsvSource(value = {
            "'', false, invalid input! 1.The input cannot be empty 2.Input should be separated ' / ' 3.You must enter only numbers 4.You must write the input as in the example '50 / 50'",
            "'a', false, invalid input! 1.The input cannot be empty 2.Input should be separated ' / ' 3.You must enter only numbers 4.You must write the input as in the example '50 / 50'",
            "'5', false, invalid input! 1.The input cannot be empty 2.Input should be separated ' / ' 3.You must enter only numbers 4.You must write the input as in the example '50 / 50'",
            "'5 5', false, invalid input! 1.The input cannot be empty 2.Input should be separated ' / ' 3.You must enter only numbers 4.You must write the input as in the example '50 / 50'",
            "'50/ 50', false, invalid input! 1.The input cannot be empty 2.Input should be separated ' / ' 3.You must enter only numbers 4.You must write the input as in the example '50 / 50'",
            "'50 / 50', true,",
    })
    void shouldSuccessfullyValidTwoId(String input, boolean isValid, String errorMessage) {
        if (isValid) {
            assertThat(ValidatorService.isTwoIdValid(input)).isTrue();
        } else {
            var exception = assertThrows(ValidatorServiceException.class,
                    () -> ValidatorService.isTwoIdValid(input));
            assertThat(errorMessage).isEqualTo(exception.getLocalizedMessage());
        }
    }
}
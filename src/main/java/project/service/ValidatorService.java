package project.service;

import org.apache.commons.lang3.StringUtils;
import project.exception.LibraryServiceException;
import project.exception.ValidatorServiceException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorService {

    public static boolean isInputAddBookValid(String input) {
        if (input == null || input.length() == 0) {
            throw new ValidatorServiceException("input cannot be empty!");
        } else if (StringUtils.countMatches(input, " / ") != 1) {
            throw new LibraryServiceException("input should be separated ' / '!");
        }
        return true;
    }

    public static boolean isInputAddReaderValid(String input) {
        if (input == null || input.length() == 0) {
            throw new ValidatorServiceException("input cannot be empty!");
        }
        return true;
    }

    public static boolean isNameReaderOrAuthorValid(String name) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]{2,}$");
        Matcher matcher = pattern.matcher(name);

        if (!matcher.find()) {
            throw new ValidatorServiceException("""
                    invalid name!
                                            
                    1. Name must contain only letters
                    2. Have more than two letters
                    3. Maximum number of letters 100
                                            
                    Fro example 'Danyl Zanuk'""");
        }
        return true;
    }

    public static boolean isOneIdValid(String input) {
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(input);

        if (!matcher.find()) {
            throw new ValidatorServiceException("""                       
                    invalid input!
                                            
                    1. The input cannot be empty
                    2. You must enter only number
                    3. The input must match the example

                    Fro example '5'""");
        }
        return true;
    }

    public static boolean isTwoIdValid(String input) {
        Pattern pattern = Pattern.compile("^\\d+\\s\\/\\s\\d+");
        Matcher matcher = pattern.matcher(input);

        if (!matcher.find()) {
            throw new ValidatorServiceException("""                       
                    invalid input!
                                            
                    1. The input cannot be empty
                    2. Input should be separated ' / '
                    2. You must enter only numbers
                    3. The input must match the example

                    Fro example '50 / 50'""");
        }
        return true;
    }
}

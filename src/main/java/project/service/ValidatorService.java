package project.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorService {

    public static boolean validateName(String name) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]{2,}$");
        Matcher matcher = pattern.matcher(name);

        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateOneId(String input) {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateTwoId(String input) {
        Pattern pattern = Pattern.compile("^\\d+\\s\\/\\s\\d+");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }
}

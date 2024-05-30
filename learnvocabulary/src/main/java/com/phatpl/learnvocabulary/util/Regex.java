package com.phatpl.learnvocabulary.util;

import java.util.regex.Pattern;

public class Regex {
    /*
        https://regexr.com/
        Username:
            -Must start with an alphabetic character. Can contain the following characters: a-z A-Z 0-9 - and _
        Email:
            - at least 8 characters
            - must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number
            - Can contain special characters

     */
    public static final String REGEX_EMAIL = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    public static final String REGEX_USERNAME = "/[a-zA-Z][a-zA-Z0-9-_]{3,32}/gi";
    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";

    public static boolean checkRegex(String text, final String regex) {
        return Pattern.compile(regex).matcher(text).matches();
    }
}

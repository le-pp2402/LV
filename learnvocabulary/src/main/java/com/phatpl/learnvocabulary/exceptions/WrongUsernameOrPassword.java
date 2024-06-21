package com.phatpl.learnvocabulary.exceptions;

public class WrongUsernameOrPassword extends RuntimeException {
    public WrongUsernameOrPassword() {
        super("wrong username or password");
    }
}

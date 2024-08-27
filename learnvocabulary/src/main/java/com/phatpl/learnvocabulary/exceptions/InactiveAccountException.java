package com.phatpl.learnvocabulary.exceptions;

public class InactiveAccountException extends RuntimeException {
    public InactiveAccountException() {
        super("inactive account");
    }
}

package com.phatpl.learnvocabulary.exceptions;

public class ExistedException extends RuntimeException {
    public ExistedException(String object) {
        super(object + "existed");
    }
}

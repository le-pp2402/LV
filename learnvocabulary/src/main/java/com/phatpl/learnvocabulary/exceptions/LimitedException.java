package com.phatpl.learnvocabulary.exceptions;

public class LimitedException extends RuntimeException {
    public LimitedException(String str) {
        super(str + " is maximum");
    }
}

package com.phatpl.learnvocabulary.exceptions;

public class ExpiredException extends RuntimeException {
    public ExpiredException(String str) {
        super(str + "expired");
    }
}

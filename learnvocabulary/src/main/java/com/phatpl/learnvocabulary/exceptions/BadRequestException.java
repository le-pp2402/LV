package com.phatpl.learnvocabulary.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String str) {
        super(str);
    }
}

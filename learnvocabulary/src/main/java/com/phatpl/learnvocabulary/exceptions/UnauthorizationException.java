package com.phatpl.learnvocabulary.exceptions;

public class UnauthorizationException extends RuntimeException {
    public UnauthorizationException(String str) {
        super(str);
    }
}

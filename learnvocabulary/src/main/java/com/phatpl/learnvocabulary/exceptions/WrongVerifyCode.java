package com.phatpl.learnvocabulary.exceptions;

public class WrongVerifyCode extends RuntimeException {
    public WrongVerifyCode() {
        super("wrong code");
    }

}

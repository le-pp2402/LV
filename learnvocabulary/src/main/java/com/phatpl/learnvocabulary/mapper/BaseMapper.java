package com.phatpl.learnvocabulary.mapper;

public interface BaseMapper <T, S> {
    public S convert(T dto);
}

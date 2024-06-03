package com.phatpl.learnvocabulary.mapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public interface BaseMapper <T, S> {
    public S convert(T dto);
}

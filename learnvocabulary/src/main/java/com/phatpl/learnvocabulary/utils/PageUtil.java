package com.phatpl.learnvocabulary.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

// PAG
public class PageUtil {
    public static PageRequest build(Integer pageNumber, Integer pageSize) {
        if (pageNumber == null) pageNumber = Constant.PAGE_NUMBER;
        if (pageSize == null) pageSize = Constant.PAGE_SIZE;
        return PageRequest.of(pageNumber, pageSize);
    }

    public static  PageRequest build(Integer pageNumber, Integer pageSize, Sort sort) {
        if (pageNumber == null) pageNumber = Constant.PAGE_NUMBER;
        if (pageSize == null) pageSize = Constant.PAGE_SIZE;
        return PageRequest.of(pageNumber, pageSize, sort);
    }
}



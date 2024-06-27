package com.phatpl.learnvocabulary.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseFilter {
    private Integer pageSize;
    private Integer pageNumber;
    private String sortBy;

    public Pageable getPageable() {
        return PageRequest.of(pageNumber, pageSize);
    }
}

package com.phatpl.learnvocabulary.filters;

import com.phatpl.learnvocabulary.utils.PageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
        return PageUtil.build(pageNumber, pageSize);
    }
}

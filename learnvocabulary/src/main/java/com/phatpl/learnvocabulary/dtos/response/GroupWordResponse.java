package com.phatpl.learnvocabulary.dtos.response;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupWordResponse extends BaseDTO {
    private Integer wordId;
    private Integer groupId;
}

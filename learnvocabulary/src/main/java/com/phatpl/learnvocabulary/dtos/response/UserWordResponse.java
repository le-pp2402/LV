package com.phatpl.learnvocabulary.dtos.response;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserWordResponse extends BaseDTO {
    private Integer wordId;
    private String word;
    private Integer rank;
}

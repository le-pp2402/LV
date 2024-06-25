package com.phatpl.learnvocabulary.dtos.response;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponse extends BaseDTO {
    String name;
    Boolean isPrivate;
}
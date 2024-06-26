package com.phatpl.learnvocabulary.dtos.response;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordResponse extends BaseDTO {
    String word;
    String phonetic;
    String definition;
    String example;
}

package com.phatpl.learnvocabulary.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Friend extends BaseModel {
    Integer userId1;
    Integer userId2;
    Integer status;
}

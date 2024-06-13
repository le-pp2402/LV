package com.phatpl.learnvocabulary.dtos.request;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyEmailRequest {
    private Integer id;
    private Integer code;
}

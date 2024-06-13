package com.phatpl.learnvocabulary.dtos.request;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest extends BaseDTO {
    private String username;
    private String password;
}

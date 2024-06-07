package com.phatpl.learnvocabulary.dtos.response;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse extends BaseDTO {
    private Integer id;
    private String username;
    private String email;
    private Boolean isAdmin;
    private Integer elo;
}

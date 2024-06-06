package com.phatpl.learnvocabulary.dtos.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String username;
    private String email;
    private Boolean isAdmin;
    private Integer elo;
}

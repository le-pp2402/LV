package com.phatpl.learnvocabulary.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo {
    private Integer id;
    private String username;
    private String email;
    private Boolean isAdmin;
    private Integer elo;
}

package com.phatpl.learnvocabulary.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("is_admin")
    private Boolean isAdmin;
    private Integer elo;

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", elo=" + elo +
                '}';
    }
}

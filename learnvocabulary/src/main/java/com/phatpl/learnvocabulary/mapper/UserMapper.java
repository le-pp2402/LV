package com.phatpl.learnvocabulary.mapper;

import com.phatpl.learnvocabulary.dto.response.UserInfo;
import com.phatpl.learnvocabulary.model.User;


public class UserMapper {
    public static UserInfo convertToUserInfo(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .elo(user.getElo())
                .isAdmin(user.getIsAdmin())
                .build();
    }
}

package com.phatpl.learnvocabulary.mapper;

import com.phatpl.learnvocabulary.dto.response.UserResponse;
import com.phatpl.learnvocabulary.model.User;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserMapper implements BaseMapper<User, UserResponse> {

    @Override
    public UserResponse convert(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .isAdmin(user.getIsAdmin())
                .elo(user.getElo())
                .build();
    }

    public List<UserResponse> convert(List<User> users) {
        var listUser = new ArrayList<UserResponse>();
        for (User user: users) {
            listUser.add(convert(user));
        }
        return listUser;
    }
}

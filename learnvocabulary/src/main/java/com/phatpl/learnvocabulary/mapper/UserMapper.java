package com.phatpl.learnvocabulary.mapper;

import com.phatpl.learnvocabulary.dto.request.RegisterRequest;
import com.phatpl.learnvocabulary.dto.response.UserResponse;
import com.phatpl.learnvocabulary.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper instance = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    RegisterRequest userToRegisterRequest(User user);
    UserResponse userToUserResponse(User user);
    List<UserResponse> userToUserResponse(List<User> users);
    User registerRequestToUser(RegisterRequest registerRequest);
}

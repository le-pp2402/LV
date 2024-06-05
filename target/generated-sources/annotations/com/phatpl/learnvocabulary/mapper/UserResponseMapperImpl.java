package com.phatpl.learnvocabulary.mapper;

import com.phatpl.learnvocabulary.dto.response.UserResponse;
import com.phatpl.learnvocabulary.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-06T02:27:31+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class UserResponseMapperImpl implements UserResponseMapper {

    @Override
    public UserResponse toDTO(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.username( entity.getUsername() );
        userResponse.email( entity.getEmail() );
        userResponse.isAdmin( entity.getIsAdmin() );
        userResponse.elo( entity.getElo() );

        return userResponse.build();
    }

    @Override
    public User toEntity(UserResponse dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( dto.getUsername() );
        user.email( dto.getEmail() );
        user.isAdmin( dto.getIsAdmin() );
        user.elo( dto.getElo() );

        return user.build();
    }

    @Override
    public List<UserResponse> toListDTO(List<User> e) {
        if ( e == null ) {
            return null;
        }

        List<UserResponse> list = new ArrayList<UserResponse>( e.size() );
        for ( User user : e ) {
            list.add( toDTO( user ) );
        }

        return list;
    }

    @Override
    public List<User> toListEntity(List<UserResponse> dto) {
        if ( dto == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( dto.size() );
        for ( UserResponse userResponse : dto ) {
            list.add( toEntity( userResponse ) );
        }

        return list;
    }
}

package com.phatpl.learnvocabulary.mappers;

import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.models.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-21T15:35:28+0700",
    comments = "version: 1.6.0.Beta2, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class UserResponseMapperImpl implements UserResponseMapper {

    @Override
    public UserResponse toDTO(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setId( entity.getId() );
        userResponse.setCreatedAt( entity.getCreatedAt() );
        userResponse.setUpdatedAt( entity.getUpdatedAt() );
        userResponse.setUsername( entity.getUsername() );
        userResponse.setEmail( entity.getEmail() );
        userResponse.setIsAdmin( entity.getIsAdmin() );
        userResponse.setElo( entity.getElo() );

        return userResponse;
    }

    @Override
    public User toEntity(UserResponse dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setId( dto.getId() );
        user.setCreatedAt( dto.getCreatedAt() );
        user.setUpdatedAt( dto.getUpdatedAt() );
        user.setUsername( dto.getUsername() );
        user.setEmail( dto.getEmail() );
        user.setIsAdmin( dto.getIsAdmin() );
        user.setElo( dto.getElo() );

        return user;
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

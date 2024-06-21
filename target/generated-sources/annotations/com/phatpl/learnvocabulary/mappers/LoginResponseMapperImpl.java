package com.phatpl.learnvocabulary.mappers;

import com.phatpl.learnvocabulary.dtos.response.LoginResponse;
import com.phatpl.learnvocabulary.models.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-21T15:08:00+0700",
    comments = "version: 1.6.0.Beta2, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class LoginResponseMapperImpl implements LoginResponseMapper {

    @Override
    public LoginResponse toDTO(User entity) {
        if ( entity == null ) {
            return null;
        }

        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setId( entity.getId() );
        loginResponse.setCreatedAt( entity.getCreatedAt() );
        loginResponse.setUpdatedAt( entity.getUpdatedAt() );
        loginResponse.setUsername( entity.getUsername() );
        loginResponse.setEmail( entity.getEmail() );
        loginResponse.setIsAdmin( entity.getIsAdmin() );
        loginResponse.setElo( entity.getElo() );

        return loginResponse;
    }

    @Override
    public User toEntity(LoginResponse dto) {
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
    public List<LoginResponse> toListDTO(List<User> e) {
        if ( e == null ) {
            return null;
        }

        List<LoginResponse> list = new ArrayList<LoginResponse>( e.size() );
        for ( User user : e ) {
            list.add( toDTO( user ) );
        }

        return list;
    }

    @Override
    public List<User> toListEntity(List<LoginResponse> dto) {
        if ( dto == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( dto.size() );
        for ( LoginResponse loginResponse : dto ) {
            list.add( toEntity( loginResponse ) );
        }

        return list;
    }
}

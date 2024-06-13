package com.phatpl.learnvocabulary.mappers;

import com.phatpl.learnvocabulary.dtos.request.RegisterRequest;
import com.phatpl.learnvocabulary.models.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-13T14:01:38+0700",
    comments = "version: 1.6.0.Beta2, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class RegisterRequestMapperImpl implements RegisterRequestMapper {

    @Override
    public RegisterRequest toDTO(User entity) {
        if ( entity == null ) {
            return null;
        }

        RegisterRequest registerRequest = new RegisterRequest();

        registerRequest.setUsername( entity.getUsername() );
        registerRequest.setPassword( entity.getPassword() );
        registerRequest.setEmail( entity.getEmail() );

        return registerRequest;
    }

    @Override
    public User toEntity(RegisterRequest dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( dto.getUsername() );
        user.password( dto.getPassword() );
        user.email( dto.getEmail() );

        return user.build();
    }

    @Override
    public List<RegisterRequest> toListDTO(List<User> e) {
        if ( e == null ) {
            return null;
        }

        List<RegisterRequest> list = new ArrayList<RegisterRequest>( e.size() );
        for ( User user : e ) {
            list.add( toDTO( user ) );
        }

        return list;
    }

    @Override
    public List<User> toListEntity(List<RegisterRequest> dto) {
        if ( dto == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( dto.size() );
        for ( RegisterRequest registerRequest : dto ) {
            list.add( toEntity( registerRequest ) );
        }

        return list;
    }
}

package com.phatpl.learnvocabulary.mappers;

import com.phatpl.learnvocabulary.dtos.response.UserGroupResponse;
import com.phatpl.learnvocabulary.models.UserGroup;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-21T16:18:39+0700",
    comments = "version: 1.6.0.Beta2, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class UserGroupResponseMapperImpl implements UserGroupResponseMapper {

    @Override
    public UserGroupResponse toDTO(UserGroup entity) {
        if ( entity == null ) {
            return null;
        }

        UserGroupResponse userGroupResponse = new UserGroupResponse();

        userGroupResponse.setId( entity.getId() );
        userGroupResponse.setCreatedAt( entity.getCreatedAt() );
        userGroupResponse.setUpdatedAt( entity.getUpdatedAt() );

        return userGroupResponse;
    }

    @Override
    public UserGroup toEntity(UserGroupResponse dto) {
        if ( dto == null ) {
            return null;
        }

        UserGroup.UserGroupBuilder userGroup = UserGroup.builder();

        return userGroup.build();
    }

    @Override
    public List<UserGroupResponse> toListDTO(List<UserGroup> e) {
        if ( e == null ) {
            return null;
        }

        List<UserGroupResponse> list = new ArrayList<UserGroupResponse>( e.size() );
        for ( UserGroup userGroup : e ) {
            list.add( toDTO( userGroup ) );
        }

        return list;
    }

    @Override
    public List<UserGroup> toListEntity(List<UserGroupResponse> dto) {
        if ( dto == null ) {
            return null;
        }

        List<UserGroup> list = new ArrayList<UserGroup>( dto.size() );
        for ( UserGroupResponse userGroupResponse : dto ) {
            list.add( toEntity( userGroupResponse ) );
        }

        return list;
    }
}

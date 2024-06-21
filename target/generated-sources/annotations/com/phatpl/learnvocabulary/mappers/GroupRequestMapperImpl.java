package com.phatpl.learnvocabulary.mappers;

import com.phatpl.learnvocabulary.dtos.request.CreateGroupRequest;
import com.phatpl.learnvocabulary.models.Group;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-21T15:08:01+0700",
    comments = "version: 1.6.0.Beta2, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class GroupRequestMapperImpl implements GroupRequestMapper {

    @Override
    public CreateGroupRequest toDTO(Group entity) {
        if ( entity == null ) {
            return null;
        }

        CreateGroupRequest createGroupRequest = new CreateGroupRequest();

        createGroupRequest.setId( entity.getId() );
        createGroupRequest.setCreatedAt( entity.getCreatedAt() );
        createGroupRequest.setUpdatedAt( entity.getUpdatedAt() );
        createGroupRequest.setName( entity.getName() );

        return createGroupRequest;
    }

    @Override
    public Group toEntity(CreateGroupRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Group group = new Group();

        group.setId( dto.getId() );
        group.setCreatedAt( dto.getCreatedAt() );
        group.setUpdatedAt( dto.getUpdatedAt() );
        group.setName( dto.getName() );

        return group;
    }

    @Override
    public List<CreateGroupRequest> toListDTO(List<Group> e) {
        if ( e == null ) {
            return null;
        }

        List<CreateGroupRequest> list = new ArrayList<CreateGroupRequest>( e.size() );
        for ( Group group : e ) {
            list.add( toDTO( group ) );
        }

        return list;
    }

    @Override
    public List<Group> toListEntity(List<CreateGroupRequest> dto) {
        if ( dto == null ) {
            return null;
        }

        List<Group> list = new ArrayList<Group>( dto.size() );
        for ( CreateGroupRequest createGroupRequest : dto ) {
            list.add( toEntity( createGroupRequest ) );
        }

        return list;
    }
}

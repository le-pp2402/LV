package com.phatpl.learnvocabulary.mappers;

import com.phatpl.learnvocabulary.dtos.request.CreateGroupRequest;
import com.phatpl.learnvocabulary.models.Group;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-20T04:09:42+0700",
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

        createGroupRequest.setName( entity.getName() );

        return createGroupRequest;
    }

    @Override
    public Group toEntity(CreateGroupRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Group.GroupBuilder group = Group.builder();

        group.name( dto.getName() );

        return group.build();
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

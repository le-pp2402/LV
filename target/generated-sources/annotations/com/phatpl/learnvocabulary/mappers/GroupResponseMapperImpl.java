package com.phatpl.learnvocabulary.mappers;

import com.phatpl.learnvocabulary.dtos.response.GroupResponse;
import com.phatpl.learnvocabulary.models.Group;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-21T03:00:56+0700",
    comments = "version: 1.6.0.Beta2, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class GroupResponseMapperImpl implements GroupResponseMapper {

    @Override
    public GroupResponse toDTO(Group entity) {
        if ( entity == null ) {
            return null;
        }

        GroupResponse groupResponse = new GroupResponse();

        groupResponse.setId( entity.getId() );
        groupResponse.setName( entity.getName() );
        groupResponse.setUpdatedAt( entity.getUpdatedAt() );
        groupResponse.setCreatedAt( entity.getCreatedAt() );
        groupResponse.setIsPrivate( entity.getIsPrivate() );

        return groupResponse;
    }

    @Override
    public Group toEntity(GroupResponse dto) {
        if ( dto == null ) {
            return null;
        }

        Group.GroupBuilder group = Group.builder();

        group.id( dto.getId() );
        group.name( dto.getName() );
        group.createdAt( dto.getCreatedAt() );
        group.updatedAt( dto.getUpdatedAt() );
        group.isPrivate( dto.getIsPrivate() );

        return group.build();
    }

    @Override
    public List<GroupResponse> toListDTO(List<Group> e) {
        if ( e == null ) {
            return null;
        }

        List<GroupResponse> list = new ArrayList<GroupResponse>( e.size() );
        for ( Group group : e ) {
            list.add( toDTO( group ) );
        }

        return list;
    }

    @Override
    public List<Group> toListEntity(List<GroupResponse> dto) {
        if ( dto == null ) {
            return null;
        }

        List<Group> list = new ArrayList<Group>( dto.size() );
        for ( GroupResponse groupResponse : dto ) {
            list.add( toEntity( groupResponse ) );
        }

        return list;
    }
}

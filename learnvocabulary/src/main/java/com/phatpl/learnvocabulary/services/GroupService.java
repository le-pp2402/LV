package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import com.phatpl.learnvocabulary.dtos.response.GroupResponse;
import com.phatpl.learnvocabulary.filters.GroupFilter;
import com.phatpl.learnvocabulary.mappers.BaseMapper;
import com.phatpl.learnvocabulary.mappers.GroupResponseMapper;
import com.phatpl.learnvocabulary.models.Group;
import com.phatpl.learnvocabulary.repositories.BaseRepository;
import com.phatpl.learnvocabulary.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService extends BaseService<Group, GroupResponse, GroupFilter, Integer> {
    GroupRepository groupRepository;
    GroupResponseMapper groupResponseMapper;
    @Autowired
    public GroupService(GroupResponseMapper groupResponseMapper, GroupRepository groupRepository) {
        super(groupResponseMapper, groupRepository);
        this.groupRepository = groupRepository;
        this.groupResponseMapper = groupResponseMapper;
    }


}

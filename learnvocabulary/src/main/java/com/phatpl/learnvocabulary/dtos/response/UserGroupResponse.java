package com.phatpl.learnvocabulary.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.phatpl.learnvocabulary.dtos.BaseDTO;
import com.phatpl.learnvocabulary.models.Group;

public class UserGroupResponse extends BaseDTO {
    Integer id;
    @JsonProperty("group")
    Group group;
    @JsonProperty("is_owner")
    Boolean isOwner;
}

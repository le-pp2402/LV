package com.phatpl.learnvocabulary.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.phatpl.learnvocabulary.dtos.BaseDTO;
import com.phatpl.learnvocabulary.models.Group;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGroupResponse extends BaseDTO {
    @JsonProperty("group")
    private Group group;
    @JsonProperty("is_owner")
    private Boolean isOwner;
}

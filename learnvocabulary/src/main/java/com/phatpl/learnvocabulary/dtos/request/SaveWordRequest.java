package com.phatpl.learnvocabulary.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveWordRequest {
    @JsonProperty("group_id")
    private Integer groupId;
}

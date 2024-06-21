package com.phatpl.learnvocabulary.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateGroupRequest {
    @JsonProperty("name")
    String name;
    @JsonProperty("is_private")
    Boolean isPrivate;
}

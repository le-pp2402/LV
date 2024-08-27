package com.phatpl.learnvocabulary.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGroupRequest {
    public Integer id;
    public String name;
}
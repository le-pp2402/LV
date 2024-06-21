package com.phatpl.learnvocabulary.dtos.request;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import com.phatpl.learnvocabulary.utils.Regex;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class CreateGroupRequest extends BaseDTO {
    public String name;
}
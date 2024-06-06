package com.phatpl.learnvocabulary.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
    @JsonProperty("code")
    Integer code;
    @JsonProperty("data")
    Object data;
    @JsonProperty("message")
    String message;
}

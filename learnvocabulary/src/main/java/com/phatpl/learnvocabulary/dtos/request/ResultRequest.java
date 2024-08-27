package com.phatpl.learnvocabulary.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultRequest {
    //  [WordId] - [correct / wrong]
    @JsonProperty("answer")
    List<List<Integer>> answers;

    
}

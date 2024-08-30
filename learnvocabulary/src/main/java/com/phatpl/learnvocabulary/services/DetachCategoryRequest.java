package com.phatpl.learnvocabulary.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetachCategoryRequest {
    @JsonProperty("category_id")
    private Long categoryId;
}

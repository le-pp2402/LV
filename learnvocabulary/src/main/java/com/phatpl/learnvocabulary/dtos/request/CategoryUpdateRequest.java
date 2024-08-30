package com.phatpl.learnvocabulary.dtos.request;

import com.phatpl.learnvocabulary.utils.Regex;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequest {
    private Long categoryId;

    @Pattern(regexp = Regex.CATEGORY, message = "Invalid category name")
    private String category;
}

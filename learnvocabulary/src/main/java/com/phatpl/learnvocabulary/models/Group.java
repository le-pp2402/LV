package com.phatpl.learnvocabulary.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "`groups`")
public class Group extends BaseModel {
    String name;

    @Builder.Default
    Boolean isPrivate = true;
    
}

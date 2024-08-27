package com.phatpl.learnvocabulary.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "group_word")
public class GroupWord extends BaseModel {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "word_id")
    Word word;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    Group group;
}

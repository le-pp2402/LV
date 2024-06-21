package com.phatpl.learnvocabulary.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "group_word")
public class GroupWord extends BaseModel {

    @Column(name = "group_id", insertable=false, updatable=false)
    Integer groupId;
    @Column(name = "word_id", insertable=false, updatable=false)
    Integer wordId;

    @ManyToOne
    @JoinColumn(name = "word_id")
    Word word;

    @ManyToOne
    @JoinColumn(name = "group_id")
    Group group;

}

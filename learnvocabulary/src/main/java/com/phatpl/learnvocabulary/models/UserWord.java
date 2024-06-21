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
@Table(name = "user_word")
public class UserWord extends BaseModel {

    @Column(name = "user_id", insertable=false, updatable=false)
    Integer userId;
    @Column(name = "word_id", insertable=false, updatable=false)
    Integer wordId;
    Integer rank;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}

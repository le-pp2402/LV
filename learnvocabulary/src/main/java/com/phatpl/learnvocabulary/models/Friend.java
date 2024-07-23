package com.phatpl.learnvocabulary.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "friends")
public class Friend extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "user_1_id")
    User user1;

    @ManyToOne
    @JoinColumn(name = "user_2_id")
    User user2;

    Boolean status;
}

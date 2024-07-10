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
@Table(name = "resources")
public class Resource extends BaseModel {
    @Column(nullable = false)
    String title;
    String source;
    String enSub;
    String viSub;
    Boolean isPrivate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}

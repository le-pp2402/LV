package com.phatpl.learnvocabulary.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "friends")
public class Friend implements BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "user_1_id", insertable = false, updatable = false)
    Integer user1Id;
    @Column(name = "user_2_id", insertable = false, updatable = false)
    Integer user2Id;
    Boolean status;

    @ManyToOne
    @JoinColumn(name = "user_1_id")
    User user1;

    @ManyToOne
    @JoinColumn(name = "user_2_id")
    User user2;

}

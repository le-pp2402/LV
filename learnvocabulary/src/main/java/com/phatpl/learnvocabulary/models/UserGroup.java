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
@Table(name = "user_group")
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "user_id")
    Integer userId;
    @Column(name = "group_id")
    Integer groupId;
    Boolean isOwner;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    User user;
//
//    @ManyToOne
//    @JoinColumn(name = "group_id")
//    Group group;
}

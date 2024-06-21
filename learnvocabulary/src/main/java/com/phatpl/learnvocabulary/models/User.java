package com.phatpl.learnvocabulary.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User extends BaseModel {

    @Column(length = 50, nullable = false)
    String username;

    @Column(length = 200, nullable = false)
    String password;

    @Column(nullable = false, length = 100)
    String email;

    @Column(updatable = false)
    Boolean isAdmin = false;
    Integer elo = 0;
    Boolean activated = false;

    Integer code;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<UserGroup> userGroups;
}

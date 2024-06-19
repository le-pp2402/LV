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
@Builder
@Table(name = "users")
public class User implements BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(length = 50, nullable = false)
    String username;

    @Column(length = 200, nullable = false)
    String password;

    @Column(nullable = false, length = 100)
    String email;

    @Builder.Default
    @Column(updatable = false)
    Boolean isAdmin = false;

    @Builder.Default
    Integer elo = 0;

    @Builder.Default
    Boolean actived = false;

    Integer code;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<UserGroup> userGroups;
}

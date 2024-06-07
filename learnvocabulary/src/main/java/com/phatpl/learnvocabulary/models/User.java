package com.phatpl.learnvocabulary.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "users")
@Component
public class User implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String username;
    String password;
    String email;
    @Builder.Default
    Boolean isAdmin = false;
    @Builder.Default
    Integer elo = 0;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    List<Resource> resource;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    List<UserGroup> userGroup;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<UserWord> userWord;

    @OneToMany(mappedBy = "user1")
    List<History> histories;

    @OneToMany(mappedBy = "user1")
    List<Friend> friends;

}

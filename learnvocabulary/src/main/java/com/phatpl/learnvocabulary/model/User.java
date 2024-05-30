package com.phatpl.learnvocabulary.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Component
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "elo")
    private Integer elo;
}

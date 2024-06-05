package com.phatpl.learnvocabulary.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@Component
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String email;
    @Builder.Default
    private Boolean isAdmin = false;
    @Builder.Default
    private Integer elo = 0;

}

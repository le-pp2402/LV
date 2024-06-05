package com.phatpl.learnvocabulary.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "histories")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "user_1_id")
    Integer user1Id;
    @Column(name = "user_2_id")
    Integer user2Id;
    @Column(name = "player_1_score")
    Integer pointPlayer1;
    @Column(name = "player_2_score")
    Integer pointPlayer2;
    Date createdAt;
}

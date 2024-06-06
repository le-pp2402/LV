package com.phatpl.learnvocabulary.models;

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
    @Column(name = "user_1_id", insertable=false, updatable=false)
    Integer idUser1;
    @Column(name = "user_2_id", insertable=false, updatable=false)
    Integer idUser2;
    @Column(name = "player_1_score")
    Integer pointPlayer1;
    @Column(name = "player_2_score")
    Integer pointPlayer2;
    Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_1_id")
    User user1;

    @ManyToOne
    @JoinColumn(name = "user_2_id")
    User user2;
}

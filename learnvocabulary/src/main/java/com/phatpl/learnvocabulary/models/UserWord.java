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
@Table(name = "user_word")
public class UserWord extends BaseModel {
    @Column(name = "`rank`")
    Integer rank;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "word_id")
    Word word;

    public void AnswerCorrect() {
        this.setRank(Math.min(this.getRank() + 1, 5));
    }

    public void AnswerWrong() {
        this.setRank(1);
    }

    public void ExpiredTime() {
        this.setRank(Math.max(1, this.getRank() - 1));
    }
}

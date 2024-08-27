package com.phatpl.learnvocabulary.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "words")
public class Word extends BaseModel {
    String word;
    String phonetic;
    @Column(columnDefinition = "TEXT")
    String definition;
    @Column(columnDefinition = "TEXT")
    String example;
}

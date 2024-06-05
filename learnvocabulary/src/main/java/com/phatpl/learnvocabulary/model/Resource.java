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
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;
    String title;
    Integer user_id;
    String sources;
    String en_sub;
    String vi_sub;
    Date updated_at;
    Date created_at;
    Boolean is_private;
}

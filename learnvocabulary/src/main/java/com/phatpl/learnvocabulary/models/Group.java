package com.phatpl.learnvocabulary.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "`groups`")
public class Group extends BaseModel {

    String name;

    Boolean isPrivate = true;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    List<GroupWord> wordGroups;

    @OneToMany(mappedBy = "group")
    List<UserGroup> userGroups;
}

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "`groups`")
public class Group implements BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    @CreationTimestamp
    Timestamp updatedAt;
    @UpdateTimestamp
    Timestamp createdAt;
    @Builder.Default
    Boolean isPrivate = true;

    @OneToMany(mappedBy = "groupId", fetch = FetchType.LAZY)
    List<GroupWord> groupWords;

    @ManyToMany()
    @JoinTable(name = "user_group", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> users;
}

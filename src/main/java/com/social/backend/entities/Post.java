package com.social.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Descriptive fields
    @Column(nullable = false, length=200)
    private String title;

    @Column(nullable=false, length=1000)
    private String description;

    @Column
    private Integer likes = 0;

    @Column
    private Integer dislikes = 0;

    @Column
    private Date createdAt = new Date();

    // Relationships

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();
    // comments will be added by themselves
}

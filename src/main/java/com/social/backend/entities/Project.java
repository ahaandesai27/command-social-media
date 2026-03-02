package com.social.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;                                 // I think use this ID to update

    @ManyToOne(fetch = FetchType.LAZY)              // many projects owned by same user
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // descriptive fields
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    private String githubUrl;
}

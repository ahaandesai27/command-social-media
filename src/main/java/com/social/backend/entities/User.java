package com.social.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        }
)
@Indexed
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @FullTextField(analyzer = "username_autocomplete", searchAnalyzer = "standard", projectable = Projectable.YES) // stores the fields value in search index
    @KeywordField(name = "username_sort", sortable = Sortable.YES)
    @Column(nullable = false, unique = true, length = 255)
    private String username;

    private String email;
    private String password;

    private String name;
    private String title;
    private String about;
    private String githubUsername;
    private String location;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date joinDate;

    @Column(nullable = false)
    private Integer followers;

    @Column(nullable = false)
    private Integer following;

    private String avatar;

    @ElementCollection
    private List<String> techStack = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Project> projects = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Post> posts = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> likedPosts = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_follows",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followee_id"),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = {"follower_id", "followee_id"}
            )
    )
    private Set<User> followingUsers = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        // cant just set values in field declarations because JPA might override them
        // must do prepersist to properly set fields
        if (this.joinDate == null) {
            this.joinDate = new Date();
        }
        if (this.followers == null) {
            this.followers = 0;
        }
        if (this.following == null) {
            this.following = 0;
        }
    }
}

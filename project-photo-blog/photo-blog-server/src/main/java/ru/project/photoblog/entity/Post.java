package ru.project.photoblog.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String caption;
    private String location;
    private Integer likes;

    private Set<String> likedUsers = new HashSet<>();
    private User user;
    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime createdDate;
    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}

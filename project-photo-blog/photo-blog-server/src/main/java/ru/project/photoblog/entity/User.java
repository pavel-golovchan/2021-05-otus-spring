package ru.project.photoblog.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    private String nickName;
    private String email;
    private String password;

    private List<Post> posts = new ArrayList<>();

    private LocalDateTime createdDate;
    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}

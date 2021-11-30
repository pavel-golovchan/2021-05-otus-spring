package ru.project.photoblog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * Фотографии (картинки)
 */
@Data
@Entity
@Table(name="pb_photo")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String fileName;
    @JsonIgnore
    @Column(nullable = true, name="author_user_id")
    private Long authorUserId;
    @JsonIgnore
    private Long postId;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileBody;

    public Photo() {
    }

}

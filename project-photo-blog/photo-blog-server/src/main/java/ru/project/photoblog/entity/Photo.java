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
    private Long authorUserId;
    @JsonIgnore
    private Long postId;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] fileBody;

    public Photo() {
    }

}

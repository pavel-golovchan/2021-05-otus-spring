package ru.project.photoblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.photoblog.entity.Photo;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Optional<Photo> findByAuthorUserId(Long userId);

    Optional<Photo> findByPostId(Long postId);

}
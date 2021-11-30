package ru.project.photoblog.services;

import ru.project.photoblog.entity.Photo;
import ru.project.photoblog.entity.Post;
import ru.project.photoblog.entity.User;
import ru.project.photoblog.exceptions.PhotoNotFoundException;
import ru.project.photoblog.repository.PhotoRepository;
import ru.project.photoblog.repository.PostRepository;
import ru.project.photoblog.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.project.photoblog.entity.Photo;
import ru.project.photoblog.repository.PhotoRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class PhotoUploadService {
    public static final Logger LOG = LoggerFactory.getLogger(PhotoUploadService.class);

    private PhotoRepository photoRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    @Autowired
    public PhotoUploadService(PhotoRepository photoRepositoryRepository, UserRepository userRepository, PostRepository postRepository) {
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Photo uploadPhotoToUser(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        LOG.info("Uploading photo profile to User {}", user.getUsername());

        Photo userProfilePhoto = photoRepository.findByAuthorUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfilePhoto)) {
            photoRepository.delete(userProfilePhoto);
        }

        Photo photoModel = new Photo();
        photoModel.setAuthorUserId(user.getId());
        photoModel.setFileBody(compressBytes(file.getBytes()));
        photoModel.setFileName(file.getOriginalFilename());
        return photoRepository.save(photoModel);
    }

    public Photo uploadPhotoToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        User user = getUserByPrincipal(principal);
        Post post = user.getPosts()
                .stream()
                .filter(p -> p.getId().equals(postId))
                .collect(toSinglePostCollector());

        Photo photoModel = new Photo();
        photoModel.setPostId(post.getId());
        photoModel.setFileBody(file.getBytes());
        photoModel.setFileBody(compressBytes(file.getBytes()));
        photoModel.setFileName(file.getOriginalFilename());
        LOG.info("Uploading photo to Post {}", post.getId());

        return photoRepository.save(photoModel);
    }

    public Photo getPhotoToUser(Principal principal) {
        User user = getUserByPrincipal(principal);

        Photo photoModel = photoRepository.findByAuthorUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(photoModel)) {
            photoModel.setFileBody(decompressBytes(photoModel.getFileBody()));
        }

        return photoModel;
    }

    public Photo getPhotoToPost(Long postId) {
        Photo photoModel = photoRepository.findByPostId(postId)
                .orElseThrow(() -> new PhotoNotFoundException("Cannot find photo to Post: " + postId));
        if (!ObjectUtils.isEmpty(photoModel)) {
            photoModel.setFileBody(decompressBytes(photoModel.getFileBody()));
        }

        return photoModel;
    }

    /***
     * Сжимает фото
     * @param data
     * @return
     */
    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress Bytes");
        }
        System.out.println("Compressed Photo Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    /***
     * Разжимает фото ( восстанавливает)
     * @param data
     * @return
     */
    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));

    }

    private <T> Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }
}

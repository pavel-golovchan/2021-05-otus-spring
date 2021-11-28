package ru.project.photoblog.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.photoblog.dto.PostDTO;
import ru.project.photoblog.entity.Photo;
import ru.project.photoblog.entity.Post;
import ru.project.photoblog.entity.User;
import ru.project.photoblog.exceptions.PostNotFoundException;
import ru.project.photoblog.exceptions.UserNameNotFoundException;
import ru.project.photoblog.repository.PhotoRepository;
import ru.project.photoblog.repository.PostRepository;
import ru.project.photoblog.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, PhotoRepository photoRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
    }

    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);
        LOG.info("Saving Post for User: {}", user.getEmail());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    public Post getPostById(Long postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for user email: " + user.getEmail()));
    }

    public List<Post> getAllPostForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    /**
     * Like or Dislike Post
     * @param postId
     * @param userName
     * @return
     */
    public Post likePost(Long postId, String userName) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        Optional<String> userLiked = post.getLikedUsers()
                .stream()
                .filter(u -> u.equals(userName)).findAny();

        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1); // dislike
            post.getLikedUsers().remove(userName);
        } else {
            post.setLikes(post.getLikes() + 1);  // like
            post.getLikedUsers().add(userName);
        }
        return postRepository.save(post);
    }

    public void deletePost(Long postId, Principal principal) {
        Post post = getPostById(postId, principal);
        Optional<Photo> photoModel = photoRepository.findByPostId(post.getId());
        postRepository.delete(post);
        photoModel.ifPresent(photoRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUserName(username)
                .orElseThrow(() -> new UserNameNotFoundException("Username not found with username " + username));

    }
}

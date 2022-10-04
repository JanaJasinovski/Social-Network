package com.social_network.project.service;

import com.social_network.project.dto.PostDTO;
import com.social_network.project.exception.PostNotFoundException;
import com.social_network.project.model.ImageModel;
import com.social_network.project.model.Post;
import com.social_network.project.model.User;
import com.social_network.project.repository.ImageRepository;
import com.social_network.project.repository.PostRepository;
import com.social_network.project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);

        LOG.info("Saving post for User {}", user.getEmail());

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    public Post getPostById(Long id, Principal principal) {
        User user = getUserByPrincipal(principal);

        return postRepository.findPostByIdAndUser(id, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username : " + user.getEmail()));
    }

    public List<Post> getAllPostForUser(Principal principal) {
        User user = getUserByPrincipal(principal);

        return postRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public Post likePost(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        Optional<String> userLiked = post.getLikedUsers()
                .stream()
                .filter(u -> u.equals(username)).findAny();

        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }

        return postRepository.save(post);
    }

    public void deletePost(Long id, Principal principal) {
        Post post = getPostById(id, principal);
        Optional<ImageModel> imageModel = imageRepository.findByPostId(id);
        postRepository.delete(post);
        imageModel.ifPresent(imageRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
    }
}

package com.social_network.project.repository;

import com.social_network.project.model.Comment;
import com.social_network.project.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
    Comment findByIdAndUserId(Long id, Long userId);
}

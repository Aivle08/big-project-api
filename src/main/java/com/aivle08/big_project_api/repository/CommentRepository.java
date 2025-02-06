package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost_id(Long postId);
}

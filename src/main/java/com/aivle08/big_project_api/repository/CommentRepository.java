package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

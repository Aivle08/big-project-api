package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

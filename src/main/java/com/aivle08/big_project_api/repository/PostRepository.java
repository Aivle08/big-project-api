package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Company;
import com.aivle08.big_project_api.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor_Company(Company company);
}

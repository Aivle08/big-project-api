package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByUsername(String username);
}

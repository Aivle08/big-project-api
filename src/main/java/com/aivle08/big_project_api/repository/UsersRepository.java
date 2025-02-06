package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByUsername(String username);

    Users findByUsername(String username);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByVerificationToken(String token);
}

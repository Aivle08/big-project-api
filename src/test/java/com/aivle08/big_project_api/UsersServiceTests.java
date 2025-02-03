package com.aivle08.big_project_api;

import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.repository.UsersRepository;
import com.aivle08.big_project_api.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class UsersServiceTests {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void updateUser() {
        Users users = Users.builder()
                .username("chan")
                .email("string")
                .password(passwordEncoder.encode("chan"))
                .verifiedEmail(true)
                .build();

        usersRepository.save(users);
    }
}

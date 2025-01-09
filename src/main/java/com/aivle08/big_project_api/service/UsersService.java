package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.model.entity.Users;
import com.aivle08.big_project_api.repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users createUser(Users user) {
        return usersRepository.save(user);
    }
}

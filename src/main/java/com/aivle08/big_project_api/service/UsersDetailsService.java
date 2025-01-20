package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.model.UsersDetails;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public UsersDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(usersRepository.findByUsername(username));
        return new UsersDetails(usersRepository.findByUsername(username));
    }
//        Optional<Users> optionalUser = Optional.ofNullable(usersRepository.findByUsername(username));
//
//        // ** 예외처리하세요 ** //
//        if (optionalUser.isPresent()) {
//            Users user = optionalUser.get();
//            return new UsersDetails(user);
//        } else {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//    }
}
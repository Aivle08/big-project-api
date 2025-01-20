package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.model.UsersDetails;
import com.aivle08.big_project_api.dto.input.LoginInputDTO;
import com.aivle08.big_project_api.dto.input.RegisterInputDTO;
import com.aivle08.big_project_api.repository.UsersRepository;
import com.aivle08.big_project_api.service.UsersDetailsService;
import com.aivle08.big_project_api.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private final UsersService usersService;
    private final UsersDetailsService usersDetailsService;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersController(UsersService usersService, UsersDetailsService usersDetailsService, UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersService = usersService;
        this.usersDetailsService = usersDetailsService;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterInputDTO registerInputDTO) {
        usersService.registerUser(registerInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginInputDTO loginInputDTO) {

        Users users = usersRepository.findByUsername(loginInputDTO.getUserId());
        UsersDetails userDetails = (UsersDetails) usersDetailsService.loadUserByUsername(loginInputDTO.getUserId());
        System.out.println(userDetails);

        System.out.println(userDetails.getPassword());


        if (passwordEncoder.matches(loginInputDTO.getPassword(),userDetails.getPassword())) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}

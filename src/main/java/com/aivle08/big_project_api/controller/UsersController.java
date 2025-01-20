package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.model.UsersDetails;
import com.aivle08.big_project_api.dto.input.LoginInputDTO;
import com.aivle08.big_project_api.dto.input.RegisterInputDTO;
import com.aivle08.big_project_api.repository.UsersRepository;
import com.aivle08.big_project_api.service.UsersDetailsService;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.service.UsersService;
import com.aivle08.big_project_api.util.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private final UsersService usersService;
    private final UsersDetailsService usersDetailsService;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public UsersController(UsersService usersService, UsersDetailsService usersDetailsService, UsersRepository usersRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.usersService = usersService;
        this.usersDetailsService = usersDetailsService;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterInputDTO registerInputDTO) {
        usersService.registerUser(registerInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginInputDTO loginInputDTO) {
        String jwt = jwtTokenUtil.generateToken(loginInputDTO.getUserId());
        System.out.println("jwt = " + jwt);

        return ResponseEntity.ok("Login successful!");
    }

}

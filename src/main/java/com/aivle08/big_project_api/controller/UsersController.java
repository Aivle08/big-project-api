package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.input.LoginInputDTO;
import com.aivle08.big_project_api.dto.input.RegisterInputDTO;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
public class UsersController {

    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;

    public UsersController(UsersService usersService, AuthenticationManager authenticationManager) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterInputDTO registerInputDTO) {
        usersService.registerUser(registerInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginInputDTO loginInputDTO) {
        String jwt = usersService.loginUser(loginInputDTO);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwt)
                .body("Login successful!");
    }

    @GetMapping
    public ResponseEntity<?> getUsers() {
        Users user = usersService.getCurrentUser();
        return ResponseEntity.ok().body(user);
    }

}

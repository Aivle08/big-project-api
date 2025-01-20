package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.input.RegisterInputDTO;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterInputDTO registerInputDTO) {
        usersService.registerUser(registerInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @GetMapping
    public ResponseEntity<List<Users>> getUsers() {
        return usersService.getAll();
    }
}

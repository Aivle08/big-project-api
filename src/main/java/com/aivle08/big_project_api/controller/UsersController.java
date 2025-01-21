package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.input.LoginInputDTO;
import com.aivle08.big_project_api.dto.input.RegisterInputDTO;
import com.aivle08.big_project_api.service.UsersService;
import com.aivle08.big_project_api.util.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginInputDTO loginInputDTO) {
        String jwt = usersService.loginUser(loginInputDTO);
        System.out.println(jwt);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwt)
                .body("Login successful!");
    }

}

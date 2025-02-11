package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.request.LoginRequestDTO;
import com.aivle08.big_project_api.dto.request.RegisterRequestDTO;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Auth API", description = "사용자 조회 및 회원가입 API")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        System.out.println("register start");
        usersService.registerUser(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        String jwt = usersService.loginUser(loginRequestDTO);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwt)
                .body("Login successful!");
    }

    @GetMapping
    @Operation(summary = "사용자 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<Users> getUsers() {
        Users user = usersService.getCurrentUser();
        return ResponseEntity.ok().body(user);
    }
    
    @GetMapping("/check-username")
    @Operation(summary = "아이디 중복 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 아이디"),
            @ApiResponse(responseCode = "409", description = "이미 사용중인 아이디")
    })
    public ResponseEntity<Boolean> checkUsername(@RequestParam("username") String username) {
        boolean isAvailable = usersService.checkUsername(username);
        if (!isAvailable) {
            return ResponseEntity.ok(!isAvailable);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(!isAvailable);
        }
    }

    @PostMapping("/initiate-email")
    @Operation(summary = "인증 메일 발송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 발송 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<String> initiateEmail(@RequestParam String email) {
        usersService.initiateEmailRegistration(email);
        return ResponseEntity.ok("Please check your email to verify.");
    }

    @GetMapping("/verify-email")
    @Operation(summary = "이메일 인증 코드 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공"),
            @ApiResponse(responseCode = "400", description = "이메일 인증 실패")
    })
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        boolean success = usersService.verifyEmail(token);
        if (success) {
            return ResponseEntity.ok("Email verified! Now you can complete registration.");
        } else {
            return ResponseEntity.badRequest().body("Invalid token.");
        }
    }
}

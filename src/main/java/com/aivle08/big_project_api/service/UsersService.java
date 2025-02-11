package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.request.LoginRequestDTO;
import com.aivle08.big_project_api.dto.request.RegisterRequestDTO;
import com.aivle08.big_project_api.model.Company;
import com.aivle08.big_project_api.model.Department;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.repository.CompanyRepository;
import com.aivle08.big_project_api.repository.DepartmentRepository;
import com.aivle08.big_project_api.repository.UsersRepository;
import com.aivle08.big_project_api.util.JwtTokenUtil;
import com.aivle08.big_project_api.util.PasswordValidator;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final EmailService emailService;

    public UsersService(UsersRepository usersRepository, DepartmentRepository departmentRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, EmailService emailService) {
        this.usersRepository = usersRepository;
        this.departmentRepository = departmentRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.emailService = emailService;
    }

    @Transactional
    public Users registerUser(RegisterRequestDTO registerRequestDTO) {

        if (usersRepository.existsByUsername(registerRequestDTO.getUsername())) {
            throw new IllegalArgumentException("The username already exists.");
        }

        Users user = usersRepository.findByEmail(registerRequestDTO.getEmail()).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("The email address is not verified.");
        }

        if (!user.isVerifiedEmail()) {
            throw new IllegalArgumentException("The email address is not verified.");
        }

        // **비밀번호 검증 추가**
        if (!PasswordValidator.isValid(registerRequestDTO.getPassword())) {
            throw new IllegalArgumentException("Invalid password. Password must be at least 8 characters long and include letters, numbers, and special characters.");
        }

        String encodedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());

        Company company = companyRepository.findByName(registerRequestDTO.getCompanyName())
                .orElseGet(() -> {
                    Company newCompany = Company.builder()
                            .name(registerRequestDTO.getCompanyName())
                            .build();
                    return companyRepository.save(newCompany);
                });

        Department department = departmentRepository.findByNameAndCompany(registerRequestDTO.getDepartmentName(), company)
                .orElseGet(() -> {
                    Department newDepartment = Department.builder()
                        .name(registerRequestDTO.getDepartmentName())
                        .company(company)
                        .build();
                    return departmentRepository.save(newDepartment);
                });

        departmentRepository.save(department);


        Users tempUser = Users.builder()
                .id(user.getId())
                .username(registerRequestDTO.getUserId())
                .name(registerRequestDTO.getUsername())
                .password(encodedPassword)
                .email(user.getEmail())
                .verifiedEmail(user.isVerifiedEmail())
                .position(registerRequestDTO.getPosition())
                .contact(registerRequestDTO.getContact())
                .company(company)
                .department(department)
                .build();


        return usersRepository.save(tempUser);
    }

    public String loginUser(LoginRequestDTO loginRequestDTO) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getId(), loginRequestDTO.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            String token = jwtTokenUtil.generateToken(loginRequestDTO.getId());

            return token;

        } catch (AuthenticationException ex) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
    }

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return usersRepository.findByUsername(authentication.getName());
    }

    public Boolean checkUsername(String username) {
        return usersRepository.existsByUsername(username);
    }

    public void initiateEmailRegistration(String email) {
        // 1) 이메일 중복 확인
        Users user = usersRepository.findByEmail(email).orElse(null);
        if (user != null) {
            if(user.getVerificationToken() == null) {
                throw new IllegalArgumentException("Email already in use.");
            }
        }

        String token = UUID.randomUUID().toString();

        if(user == null) {
            Users tempUser = Users.builder()
                    .email(email)
                    .verifiedEmail(false)
                    .verificationToken(token)
                    .build();

            usersRepository.save(tempUser);
        }else{
            token = user.getVerificationToken();
        }

        // 3) 이메일 전송
        emailService.sendVerificationEmail(email, token);
    }

    public boolean verifyEmail(String token) {
        // 토큰으로 사용자 찾기
        Users user = usersRepository.findByVerificationToken(token).orElse(null);
        if (user == null) {
            return false;
        }

        Users tempUser = Users.builder()
                .id(user.getId())
                .email(user.getEmail())
                .verifiedEmail(true)
                .verificationToken(null).build();

        usersRepository.save(tempUser);

        return true;
    }
}

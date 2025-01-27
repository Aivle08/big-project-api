package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.input.LoginInputDTO;
import com.aivle08.big_project_api.dto.input.RegisterInputDTO;
import com.aivle08.big_project_api.model.Company;
import com.aivle08.big_project_api.model.Department;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.repository.CompanyRepository;
import com.aivle08.big_project_api.repository.DepartmentRepository;
import com.aivle08.big_project_api.repository.UsersRepository;
import com.aivle08.big_project_api.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public UsersService(UsersRepository usersRepository, DepartmentRepository departmentRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.usersRepository = usersRepository;
        this.departmentRepository = departmentRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Transactional
    public Users registerUser(RegisterInputDTO registerInputDTO) {

        if (usersRepository.existsByUsername(registerInputDTO.getUsername())) {
            throw new IllegalArgumentException("The username already exists.");
        }

        String encodedPassword = passwordEncoder.encode(registerInputDTO.getPassword());

        Company company = companyRepository.findByName(registerInputDTO.getCompanyName())
                .orElseGet(() -> {
                    Company newCompany = new Company(null, registerInputDTO.getCompanyName(), null);
                    return companyRepository.save(newCompany);
                });

//        Department department = departmentRepository.findByNameAndCompany(registerInputDTO.getDepartmentName(), company)
//                .orElseGet(() -> {
//                    Department newDepartment = new Department(null, registerInputDTO.getDepartmentName(), company, null, null);
//                    return departmentRepository.save(newDepartment);
//                });

        Department department = new Department(null, registerInputDTO.getDepartmentName(), company, null, null);
        departmentRepository.save(department);


        Users user = new Users(
                null,
                registerInputDTO.getUserId(),
                encodedPassword,
                registerInputDTO.getUsername(),
                registerInputDTO.getEmail(),
                registerInputDTO.getPosition(),
                registerInputDTO.getContact(),
                company,
                department
        );

        return usersRepository.save(user);
    }

    public String loginUser(LoginInputDTO loginInputDTO) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginInputDTO.getId(), loginInputDTO.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            String token = jwtTokenUtil.generateToken(loginInputDTO.getId());

            return token;

        } catch (AuthenticationException ex) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
    }

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return usersRepository.findByUsername(authentication.getName());
    }
}

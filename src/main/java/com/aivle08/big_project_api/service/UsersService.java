package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.input.RegisterInputDTO;
import com.aivle08.big_project_api.model.Company;
import com.aivle08.big_project_api.model.Department;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.repository.CompanyRepository;
import com.aivle08.big_project_api.repository.DepartmentRepository;
import com.aivle08.big_project_api.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, DepartmentRepository departmentRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.departmentRepository = departmentRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Users registerUser(RegisterInputDTO registerInputDTO) {

        if (usersRepository.existsByUsername(registerInputDTO.getUsername())) {
            throw new IllegalArgumentException("The username already exists.");
        }

        String encodedPassword = passwordEncoder.encode(registerInputDTO.getPassword());

        Company company = companyRepository.findByName(registerInputDTO.getCompanyName())
                .orElseGet(() -> {
                    Company newCompany = new Company(null, registerInputDTO.getCompanyName(), registerInputDTO.getAddress(), null);
                    return companyRepository.save(newCompany);
                });

        Department department = departmentRepository.findByNameAndCompany(registerInputDTO.getDepartmentName(), company)
                .orElseGet(() -> {
                    Department newDepartment = new Department(null, registerInputDTO.getDepartmentName(), company, null, null, null);
                    return departmentRepository.save(newDepartment);
                });

        Users user = new Users(
                null,
                registerInputDTO.getUserId(),
                encodedPassword,
                registerInputDTO.getUsername(),
                registerInputDTO.getEmail(),
                registerInputDTO.getPosition(),
                registerInputDTO.getContact(),
                department
        );

        return usersRepository.save(user);
    }
}
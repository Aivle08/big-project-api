package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.input.RegisterInputDTO;
import com.aivle08.big_project_api.model.Company;
import com.aivle08.big_project_api.model.Department;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.repository.CompanyRepository;
import com.aivle08.big_project_api.repository.DepartmentRepository;
import com.aivle08.big_project_api.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;

    public UsersService(UsersRepository usersRepository, DepartmentRepository departmentRepository, CompanyRepository companyRepository) {
        this.usersRepository = usersRepository;
        this.departmentRepository = departmentRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional
    public Users registerUser(RegisterInputDTO registerInputDTO) {
        Company company = new Company(null, registerInputDTO.getCompanyName(),
                registerInputDTO.getAddress(), null);
        Company savedCompany = companyRepository.save(company);

        Department department = new Department(null, registerInputDTO.getDepartmentName(), savedCompany, null, null, null);
        Department savedDepartment = departmentRepository.save(department);

        Users user = new Users(null, registerInputDTO.getUserId(), registerInputDTO.getPassword(),
                registerInputDTO.getUsername(), registerInputDTO.getEmail(),
                registerInputDTO.getPosition(), savedDepartment);

        return usersRepository.save(user);
    }
}

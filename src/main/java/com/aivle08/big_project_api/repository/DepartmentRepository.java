package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Company;
import com.aivle08.big_project_api.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByNameAndCompany(String departmentName, Company company);
}

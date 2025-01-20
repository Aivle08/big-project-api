package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Company;
import com.aivle08.big_project_api.model.Department;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
    Optional<Department> findByNameAndCompany(String name, Company company);
}

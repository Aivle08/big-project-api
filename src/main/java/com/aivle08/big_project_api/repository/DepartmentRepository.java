package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}

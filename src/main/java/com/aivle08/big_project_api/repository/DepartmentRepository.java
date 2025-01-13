package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
}

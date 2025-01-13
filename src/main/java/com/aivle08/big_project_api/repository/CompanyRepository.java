package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}

package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
}

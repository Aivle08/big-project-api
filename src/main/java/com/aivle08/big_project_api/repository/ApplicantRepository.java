package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Applicant;
import com.aivle08.big_project_api.model.Department;
import com.aivle08.big_project_api.model.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    List<Applicant> findByRecruitmentId(Long recruitmentId);
    List<Applicant> findAllByResumeResultAndRecruitmentId(boolean resumeResult, Long recruitmentId);
}

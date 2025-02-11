package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    List<Applicant> findByRecruitmentId(Long recruitmentId);

    List<Applicant> findAllByResumeResultAndRecruitmentId(boolean resumeResult, Long recruitmentId);

    @Modifying
    @Query("UPDATE Applicant a SET a.resumeResult = true WHERE a.id = :applicantId AND a.resumeResult = false")
    int updateResumeResultToPassed(@Param("applicantId") Long applicantId);

}

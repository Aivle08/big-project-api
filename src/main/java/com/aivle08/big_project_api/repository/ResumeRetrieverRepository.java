package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.ResumeRetriever;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRetrieverRepository extends JpaRepository<ResumeRetriever, Long> {
    List<ResumeRetriever> findByApplicant_Id(Long applicantId);
}

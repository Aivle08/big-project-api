package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.EvaluationScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface EvaluationScoreRepository extends JpaRepository<EvaluationScore, Integer> {
    List<EvaluationScore> findByApplicantId(Long applicantId);
}

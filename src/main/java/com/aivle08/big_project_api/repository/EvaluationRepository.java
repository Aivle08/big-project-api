package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByRecruitment_id(Long recruitmentId);
}

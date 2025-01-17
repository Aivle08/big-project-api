package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}

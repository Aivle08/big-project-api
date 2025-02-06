package com.aivle08.big_project_api.repository;

import com.aivle08.big_project_api.model.EvaluationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EvaluationDetailRepository extends JpaRepository<EvaluationDetail, Long> {
    EvaluationDetail save(EvaluationDetail evaluationDetail);
}

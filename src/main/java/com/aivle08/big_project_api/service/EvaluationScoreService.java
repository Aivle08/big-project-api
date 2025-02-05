package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.request.EvaluationScoreRequestDTO;
import com.aivle08.big_project_api.model.Applicant;
import com.aivle08.big_project_api.model.Evaluation;
import com.aivle08.big_project_api.model.EvaluationDetail;
import com.aivle08.big_project_api.model.EvaluationScore;
import com.aivle08.big_project_api.repository.ApplicantRepository;
import com.aivle08.big_project_api.repository.EvaluationDetailRepository;
import com.aivle08.big_project_api.repository.EvaluationRepository;
import com.aivle08.big_project_api.repository.EvaluationScoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluationScoreService {

    private final EvaluationScoreRepository evaluationScoreRepository;
    private final ApplicantRepository applicantRepository;
    private final EvaluationDetailRepository evaluationDetailRepository;
    private final EvaluationRepository evaluationRepository;

    public EvaluationScoreService(EvaluationScoreRepository evaluationScoreRepository, ApplicantRepository applicantRepository, EvaluationDetailRepository evaluationDetailRepository, EvaluationRepository evaluationRepository) {
        this.evaluationScoreRepository = evaluationScoreRepository;
        this.applicantRepository = applicantRepository;
        this.evaluationDetailRepository = evaluationDetailRepository;
        this.evaluationRepository = evaluationRepository;
    }

    @Transactional
    public List<EvaluationScore> createEvaluationScoreList(List<EvaluationScoreRequestDTO> evaluationScores, Long applicantId) {

        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalArgumentException("지원자를 찾을 수 없습니다: " + applicantId));

        List<EvaluationScore> newEvaluationScores = new ArrayList<>();
        for (EvaluationScoreRequestDTO scoreDTO : evaluationScores) {

            EvaluationDetail evaluationDetail = EvaluationDetail.builder()
                    .summary(scoreDTO.getSummary())
                    .build();

            EvaluationDetail savedEvaluationDetail = evaluationDetailRepository.save(evaluationDetail);

            Evaluation evaluation = evaluationRepository.findById(scoreDTO.getEvaluationId()).get();

            EvaluationScore evaluationScore = EvaluationScore.builder()
                    .evaluation(evaluation)
                    .score(scoreDTO.getScore())
                    .evaluationDetail(savedEvaluationDetail)
                    .applicant(applicant)
                    .build();

            evaluationScoreRepository.save(evaluationScore);
            newEvaluationScores.add(evaluationScore);
        }

        return newEvaluationScores;
    }
}
package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.model.EvaluationScore;
import com.aivle08.big_project_api.repository.EvaluationScoreRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluationService {

    private final EvaluationScoreRepository evaluationScoreRepository;
    private final ApplicantService applicantService;


    public EvaluationService(EvaluationScoreRepository evaluationScoreRepository, ApplicantService applicantService) {
        this.evaluationScoreRepository = evaluationScoreRepository;
        this.applicantService = applicantService;
    }

    public Map<Long, List<Long>> getScoresByApplicantIdandRecruitmentId(Long recruitmentId) {

        Map<Long, List<Long>> scoresByApplicant = new HashMap<>();

        List<Long> applicantIds = applicantService.getApplicantIDsByRecruitmentId(recruitmentId);

        for (Long applicantId : applicantIds) {

            List<Long> scores = evaluationScoreRepository.findByApplicantId(applicantId)
                    .stream()
                    .map(EvaluationScore::getScore)
                    .toList();

            scoresByApplicant.put(applicantId, scores);
        }
        return scoresByApplicant;
    }
}
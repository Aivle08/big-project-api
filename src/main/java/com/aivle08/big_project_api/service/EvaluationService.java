package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.response.EvaluationDetailResponseDTO;
import com.aivle08.big_project_api.dto.response.EvaluationResponseDTO;
import com.aivle08.big_project_api.repository.ApplicantRepository;
import com.aivle08.big_project_api.repository.EvaluationScoreRepository;
import com.aivle08.big_project_api.repository.RecruitmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService {

    private final EvaluationScoreRepository evaluationScoreRepository;
    private final ApplicantService applicantService;
    private final RecruitmentRepository recruitmentRepository;
    private final ApplicantRepository applicantRepository;

    public EvaluationService(EvaluationScoreRepository evaluationScoreRepository, ApplicantService applicantService, RecruitmentRepository recruitmentRepository, ApplicantRepository applicantRepository) {
        this.evaluationScoreRepository = evaluationScoreRepository;
        this.applicantService = applicantService;
        this.recruitmentRepository = recruitmentRepository;
        this.applicantRepository = applicantRepository;
    }

    public EvaluationResponseDTO getScoresByApplicantIdandRecruitmentId(Long recruitmentId, Long applicantId) {
        String recruitmentTitle = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recruitmentId")).getTitle();

        List<Long> applicantIds = applicantService.getApplicantIDsByRecruitmentId(recruitmentId);

        if (!applicantIds.contains(applicantId)) {
            throw new IllegalArgumentException("ApplicantId가 없습니다.");
        }

        String applicationName = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid applicantId")).getName();

        List<EvaluationDetailResponseDTO> scoreDetails = evaluationScoreRepository.findByApplicantId(applicantId)
                .stream()
                .map(evaluationScore -> {
                    String summary = evaluationScore.getEvaluationDetail().getSummary();

                    String item = evaluationScore.getEvaluationDetail()
                            .getEvaluation()
                            .getItem();

                    return EvaluationDetailResponseDTO.builder()
                            .score(evaluationScore.getScore())
                            .summary(summary)
                            .title(item)
                            .build();
                })
                .toList();
        
        return EvaluationResponseDTO.builder()
                .recruitmentTitle(recruitmentTitle)
                .applicationName(applicationName)
                .scoreDetails(scoreDetails)
                .build();
    }
}
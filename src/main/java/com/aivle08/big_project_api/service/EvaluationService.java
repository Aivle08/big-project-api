package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.response.EvaluationDetailResponseDTO;
import com.aivle08.big_project_api.dto.response.EvaluationResponseDTO;
import com.aivle08.big_project_api.dto.response.PassedApplicantResponseDTO;
import com.aivle08.big_project_api.model.Applicant;
import com.aivle08.big_project_api.model.EvaluationDetail;
import com.aivle08.big_project_api.model.EvaluationScore;
import com.aivle08.big_project_api.repository.ApplicantRepository;
import com.aivle08.big_project_api.repository.EvaluationDetailRepository;
import com.aivle08.big_project_api.repository.EvaluationScoreRepository;
import com.aivle08.big_project_api.repository.RecruitmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluationService {

    private final EvaluationScoreRepository evaluationScoreRepository;
    private final ApplicantService applicantService;
    private final RecruitmentRepository recruitmentRepository;
    private final ApplicantRepository applicantRepository;
    private final EvaluationDetailRepository evaluationDetailRepository;

    public EvaluationService(EvaluationScoreRepository evaluationScoreRepository, ApplicantService applicantService, RecruitmentRepository recruitmentRepository, ApplicantRepository applicantRepository, EvaluationDetailRepository evaluationDetailRepository) {
        this.evaluationScoreRepository = evaluationScoreRepository;
        this.applicantService = applicantService;
        this.recruitmentRepository = recruitmentRepository;
        this.applicantRepository = applicantRepository;
        this.evaluationDetailRepository = evaluationDetailRepository;
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
                    return EvaluationDetailResponseDTO.builder()
                            .score(evaluationScore.getScore())
                            .summary(summary)
                            .build();
                })
                .toList();

        return EvaluationResponseDTO.builder()
                .recruitmentTitle(recruitmentTitle)
                .applicationName(applicationName)
                .scoreDetails(scoreDetails)
                .build();
    }

    public PassedApplicantResponseDTO getPassedApplicants(Long recruitmentId) {

        String recruitmentTitle = recruitmentRepository.findById(recruitmentId)
                .map(r -> r.getTitle())
                .orElseThrow(() -> new IllegalArgumentException("Invalid recruitmentId"));

        List<Applicant> passedApplicants = applicantRepository.findAllByResumeResultAndRecruitmentId(true, recruitmentId);

        List<EvaluationResponseDTO> passList = new ArrayList<>();
        for (Applicant applicant : passedApplicants) {
            List<EvaluationDetailResponseDTO> scoreDetails = new ArrayList<>();

            for (EvaluationScore evaluationScore : applicant.getEvaluationScoreList()) {
                scoreDetails.add(EvaluationDetailResponseDTO.builder()
                        .score(evaluationScore.getScore())
                        .summary(evaluationScore.getEvaluationDetail().getSummary())
                        .title(evaluationScore.getEvaluation().getItem())
                        .build());
            }

            passList.add(EvaluationResponseDTO.builder()
                    .recruitmentTitle(recruitmentTitle)
                    .applicationName(applicant.getName())
                    .scoreDetails(scoreDetails)
                    .build());
        }

        return PassedApplicantResponseDTO.builder()
                .recruitmentTitle(recruitmentTitle)
                .passList(passList)
                .build();
    }

    public List<EvaluationResponseDTO> getAllApplicantEvaluations(Long recruitmentId) {

        String recruitmentTitle = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recruitmentId")).getTitle();

        List<Applicant> passedApplicants = applicantRepository.findByRecruitmentId(recruitmentId);

        List<EvaluationResponseDTO> allList = new ArrayList<>();
        for (Applicant applicant : passedApplicants) {
            List<EvaluationDetailResponseDTO> scoreDetails = new ArrayList<>();

            for (EvaluationScore evaluationScore : applicant.getEvaluationScoreList()) {
                scoreDetails.add(EvaluationDetailResponseDTO.builder()
                        .score(evaluationScore.getScore())
                        .summary(evaluationScore.getEvaluationDetail().getSummary())
                        .title(evaluationScore.getEvaluation().getItem())
                        .build());
            }

            allList.add(EvaluationResponseDTO.builder()
                    .recruitmentTitle(recruitmentTitle)
                    .applicationName(applicant.getName())
                    .scoreDetails(scoreDetails)
                    .build());
        }

        return allList;
    }
}
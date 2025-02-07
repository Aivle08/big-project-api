package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.request.ApplicantRequestDTO;
import com.aivle08.big_project_api.dto.response.EvaluationDetailResponseDTO;
import com.aivle08.big_project_api.dto.response.EvaluationResponseDTO;
import com.aivle08.big_project_api.model.Applicant;
import com.aivle08.big_project_api.model.EvaluationScore;
import com.aivle08.big_project_api.repository.ApplicantRepository;
import com.aivle08.big_project_api.repository.EvaluationDetailRepository;
import com.aivle08.big_project_api.repository.EvaluationScoreRepository;
import com.aivle08.big_project_api.repository.RecruitmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public EvaluationResponseDTO getScoreListByApplicantIdAndRecruitmentId(Long recruitmentId, Long applicantId) {

        String recruitmentTitle = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recruitmentId")).getTitle();

        List<Long> applicantIds = applicantService.getApplicantIdListByRecruitmentId(recruitmentId);

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

    public List<EvaluationResponseDTO> getPassedApplicantList(Long recruitmentId) {

        String recruitmentTitle = recruitmentRepository.findById(recruitmentId)
                .map(r -> r.getTitle())
                .orElseThrow(() -> new IllegalArgumentException("Invalid recruitmentId"));

        List<Applicant> passedApplicants = applicantRepository.findAllByResumeResultAndRecruitmentId(true, recruitmentId);

        List<EvaluationResponseDTO> passList = new ArrayList<>();
        for (Applicant applicant : passedApplicants) {
            List<EvaluationDetailResponseDTO> scoreDetails = new ArrayList<>();

            for (EvaluationScore evaluationScore : applicant.getEvaluationScoreList()) {

                EvaluationDetailResponseDTO evaluationDetailResponseDTO = EvaluationDetailResponseDTO.builder()
                        .score(evaluationScore.getScore())
                        .summary(evaluationScore.getEvaluationDetail().getSummary())
                        .title(evaluationScore.getEvaluation().getItem())
                        .build();

                scoreDetails.add(evaluationDetailResponseDTO);
            }

            EvaluationResponseDTO evaluationResponseDTO = EvaluationResponseDTO.builder()
                    .recruitmentTitle(recruitmentTitle)
                    .applicationName(applicant.getName())
                    .scoreDetails(scoreDetails)
                    .applicantId(applicant.getId())
                    .build();

            passList.add(evaluationResponseDTO);
        }
        return passList;
    }

    public List<EvaluationResponseDTO> getApplicantEvaluationList(Long recruitmentId) {

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

    @Transactional
    public List<ApplicantRequestDTO> getPassApplicantById(List<Long> applicantIdList) {

        for (Long applicantId : applicantIdList) {
            int updatedCount = applicantRepository.updateResumeResultToPassed(applicantId);

            if (updatedCount == 0) {
                throw new IllegalArgumentException("지원자 ID " + applicantId + "가 존재하지 않거나 이미 합격 처리되었습니다.");
            }
        }

        List<Applicant> updatedApplicants = applicantRepository.findAllById(applicantIdList);

        return updatedApplicants.stream()
                .map(ApplicantRequestDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
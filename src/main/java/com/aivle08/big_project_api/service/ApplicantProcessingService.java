package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.constants.ProcessingStatus;
import com.aivle08.big_project_api.dto.api.response.ApiResponseDTO;
import com.aivle08.big_project_api.dto.api.response.ExtractionResponseDTO;
import com.aivle08.big_project_api.model.Applicant;
import com.aivle08.big_project_api.model.Recruitment;
import com.aivle08.big_project_api.repository.ApplicantRepository;
import com.aivle08.big_project_api.repository.RecruitmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ApplicantProcessingService {

    private final ApiService apiService;
    private final ApplicantRepository applicantRepository;
    private final ObjectMapper objectMapper;
    private final RecruitmentRepository recruitmentRepository;

    public ApplicantProcessingService(ApiService apiService, ApplicantRepository applicantRepository, RecruitmentRepository recruitmentRepository) {
        this.apiService = apiService;
        this.applicantRepository = applicantRepository;
        this.recruitmentRepository = recruitmentRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Async
    public CompletableFuture<Void> processApplicant(Applicant applicant) {
        try {
            // 트랜잭션 적용된 메서드를 호출하여 DB 업데이트 수행
            updateApplicantProcessingStatus(applicant.getId(), ProcessingStatus.IN_PROGRESS);

            // 외부 API 호출
            ApiResponseDTO<ExtractionResponseDTO> apiResponseDTO = apiService.callSummaryExtractionApi(applicant.getId());
            ExtractionResponseDTO dto = objectMapper.convertValue(apiResponseDTO.getItem(), ExtractionResponseDTO.class);

            // API 응답을 바탕으로 지원자 정보 업데이트
            updateApplicantWithApiResponse(applicant.getId(), dto, ProcessingStatus.COMPLETED);
        } catch (Exception e) {
            updateApplicantProcessingStatus(applicant.getId(), ProcessingStatus.FAILED);
        }

        // 지원자 처리 후 전체 공고 상태 업데이트
        updateRecruitmentStatus(applicant.getRecruitment().getId());

        return CompletableFuture.completedFuture(null);
    }

    @Transactional
    public void updateRecruitmentStatus(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new RuntimeException("Recruitment not found"));

        List<Applicant> applicants = applicantRepository.findByRecruitmentId(recruitmentId);

        boolean allCompleted = applicants.stream().allMatch(a -> a.getProcessingStatus() == ProcessingStatus.COMPLETED);
        boolean hasFailed = applicants.stream().anyMatch(a -> a.getProcessingStatus() == ProcessingStatus.FAILED);

        if (allCompleted) {
            recruitment.updateProcessingStatus(ProcessingStatus.COMPLETED);
        } else if (hasFailed) {
            recruitment.updateProcessingStatus(ProcessingStatus.FAILED);
        } else {
            recruitment.updateProcessingStatus(ProcessingStatus.IN_PROGRESS);
        }

        recruitmentRepository.save(recruitment);
    }

    @Transactional
    public void updateApplicantProcessingStatus(Long applicantId, ProcessingStatus status) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));
        applicant.updateProcessingStatus(status);
        applicantRepository.save(applicant);
    }

    @Transactional
    public void updateApplicantWithApiResponse(Long applicantId, ExtractionResponseDTO dto, ProcessingStatus status) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        applicant.updateWithApiResponse(dto, status);
        applicantRepository.save(applicant);
    }
}

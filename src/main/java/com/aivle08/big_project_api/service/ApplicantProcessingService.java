package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.constants.ProcessingStatus;
import com.aivle08.big_project_api.dto.api.request.ScoreRequestDTO;
import com.aivle08.big_project_api.dto.api.response.ApiResponseDTO;
import com.aivle08.big_project_api.dto.api.response.ExtractionResponseDTO;
import com.aivle08.big_project_api.dto.api.response.ScoreResponseDTO;
import com.aivle08.big_project_api.model.*;
import com.aivle08.big_project_api.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ApplicantProcessingService {

    private final ApiService apiService;
    private final ApplicantRepository applicantRepository;
    private final ObjectMapper objectMapper;
    private final RecruitmentRepository recruitmentRepository;
    private final EvaluationScoreRepository evaluationScoreRepository;
    private final ResumeRetrieverRepository resumeRetrieverRepository;
    private final EvaluationDetailRepository evaluationDetailRepository;

    public ApplicantProcessingService(ApiService apiService, ApplicantRepository applicantRepository, RecruitmentRepository recruitmentRepository, EvaluationScoreRepository evaluationScoreRepository, ResumeRetrieverRepository resumeRetrieverRepository, EvaluationDetailRepository evaluationDetailRepository) {
        this.apiService = apiService;
        this.applicantRepository = applicantRepository;
        this.recruitmentRepository = recruitmentRepository;
        this.evaluationScoreRepository = evaluationScoreRepository;
        this.resumeRetrieverRepository = resumeRetrieverRepository;
        this.evaluationDetailRepository = evaluationDetailRepository;
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
    public void updateRecruitmentScoreStatus(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new RuntimeException("Recruitment not found"));

        List<Applicant> applicants = applicantRepository.findByRecruitmentId(recruitmentId);

        boolean allCompleted = applicants.stream().allMatch(a -> a.getScoreProcessingStatus() == ProcessingStatus.COMPLETED);
        boolean hasFailed = applicants.stream().anyMatch(a -> a.getScoreProcessingStatus() == ProcessingStatus.FAILED);

        if (allCompleted) {
            recruitment.updateScoreProcessingStatus(ProcessingStatus.COMPLETED);
        } else if (hasFailed) {
            recruitment.updateScoreProcessingStatus(ProcessingStatus.FAILED);
        } else {
            recruitment.updateScoreProcessingStatus(ProcessingStatus.IN_PROGRESS);
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
    public void updateApplicantScoreProcessingStatus(Long applicantId, ProcessingStatus status) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));
        applicant.updateScoreProcessingStatus(status);
        applicantRepository.save(applicant);
    }

    @Transactional
    public void updateApplicantWithApiResponse(Long applicantId, ExtractionResponseDTO dto, ProcessingStatus status) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        applicant.updateWithApiResponse(dto, status);
        applicantRepository.save(applicant);
    }

    @Transactional  // 개별 작업마다 트랜잭션 적용 (필요에 따라 범위를 조절)
    public CompletableFuture<Void> processEvaluationScoreAsync(EvaluationScore e, String job) {
        try {
            // 0. 시작 시, 지원자의 scoreProcessingStatus를 IN_PROGRESS로 업데이트
            updateApplicantScoreProcessingStatus(e.getApplicant().getId(), ProcessingStatus.IN_PROGRESS);

            // 1. ScoreRequestDTO 생성
            ScoreRequestDTO scoreRequestDTO = ScoreRequestDTO.builder()
                    .applicantId(e.getApplicant().getId())
                    .evalItem(e.getEvaluation().getItem())
                    .evalItemContent(e.getEvaluation().getDetail())
                    .job(job)
                    .build();

            // 2. 외부 API 호출 (동기적으로 block() 호출 – 비동기 메서드 내에서 별도의 트랜잭션으로 처리됨)
            ApiResponseDTO<ScoreResponseDTO> returnDto = apiService.callScoreApi(scoreRequestDTO);
            ScoreResponseDTO dto = returnDto.getItem();

            // 3. EvaluationScore 업데이트 (새로운 점수를 적용)
            EvaluationScore updatedScore = EvaluationScore.builder()
                    .id(e.getId())
                    .score(dto.getScore())
                    .applicant(e.getApplicant())
                    .evaluation(e.getEvaluation())
                    .evaluationDetail(e.getEvaluationDetail())
                    .build();
            EvaluationScore savedScore = evaluationScoreRepository.save(updatedScore);

            // 4. EvaluationDetail 업데이트 (요약 정보를 저장)
            EvaluationDetail updatedDetail = EvaluationDetail.builder()
                    .id(savedScore.getId())
                    .summary(dto.getReason())
                    .build();
            evaluationDetailRepository.save(updatedDetail);

            // 5. 응답 DTO의 chunk 항목들을 순회하며 ResumeRetriever 저장
            for (String chunk : dto.getChunk()) {
                ResumeRetriever rr = ResumeRetriever.builder()
                        .chunk(chunk)
                        .applicant(e.getApplicant())
                        .build();
                resumeRetrieverRepository.save(rr);
            }

            // 6. 정상 처리 후 scoreProcessingStatus를 COMPLETED로 업데이트
            updateApplicantScoreProcessingStatus(e.getApplicant().getId(), ProcessingStatus.COMPLETED);
        } catch (Exception ex) {
            ex.printStackTrace();
            // 예외 발생 시 scoreProcessingStatus를 FAILED로 업데이트
            updateApplicantScoreProcessingStatus(e.getApplicant().getId(), ProcessingStatus.FAILED);
        }
        return CompletableFuture.completedFuture(null);
    }


    @Async
    public CompletableFuture<Void> processApplicantScore(Applicant applicant, String job) {
        try {
            // 1. 시작 시, 해당 지원자의 scoreProcessingStatus를 IN_PROGRESS로 업데이트
            updateApplicantScoreProcessingStatus(applicant.getId(), ProcessingStatus.IN_PROGRESS);

            // 2. 해당 지원자의 모든 EvaluationScore를 가져와서 비동기 처리
            List<EvaluationScore> evaluationScores = evaluationScoreRepository.findByApplicantId(applicant.getId());
            if (evaluationScores == null || evaluationScores.isEmpty()) {
                // 평가 점수가 없는 경우, 바로 COMPLETED로 업데이트
                updateApplicantScoreProcessingStatus(applicant.getId(), ProcessingStatus.COMPLETED);
                return CompletableFuture.completedFuture(null);
            }

            List<CompletableFuture<Void>> futures = evaluationScores.stream()
                    .map(e -> processEvaluationScoreAsync(e, job))
                    .collect(Collectors.toList());

            // 3. 모든 비동기 작업이 완료될 때까지 대기
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            // 4. 모든 평가 작업이 완료되면 scoreProcessingStatus를 COMPLETED로 업데이트
            updateApplicantScoreProcessingStatus(applicant.getId(), ProcessingStatus.COMPLETED);
        } catch (Exception ex) {
            ex.printStackTrace();
            // 예외 발생 시 scoreProcessingStatus를 FAILED로 업데이트
            updateApplicantScoreProcessingStatus(applicant.getId(), ProcessingStatus.FAILED);
        }
        return CompletableFuture.completedFuture(null);
    }


}

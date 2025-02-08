package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.api.request.PdfInfoDTO;
import com.aivle08.big_project_api.dto.api.request.PdfInfoListRequestDTO;
import com.aivle08.big_project_api.dto.api.request.ScoreRequestDTO;
import com.aivle08.big_project_api.dto.api.response.ApiResponseDTO;
import com.aivle08.big_project_api.dto.api.response.ScoreResponseDTO;
import com.aivle08.big_project_api.model.*;
import com.aivle08.big_project_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ApiPipeService {

    private final ApplicantRepository applicantRepository;
    private final ApiService apiService;
    private final ApplicantProcessingService applicantProcessingService;
    private final EvaluationScoreRepository evaluationScoreRepository;
    private final EvaluationDetailRepository evaluationDetailRepository;
    private final ResumeRetrieverRepository resumeRetrieverRepository;
    private final EvaluationRepository evaluationRepository;
    @Autowired
    @Lazy
    private ApiPipeService self;

    public ApiPipeService(ApplicantRepository applicantRepository, ApiService apiService, ApplicantProcessingService applicantProcessingService, EvaluationScoreRepository evaluationScoreRepository, EvaluationDetailRepository evaluationDetailRepository, ResumeRetrieverRepository resumeRetrieverRepository, EvaluationRepository evaluationRepository) {
        this.applicantRepository = applicantRepository;
        this.apiService = apiService;
        this.applicantProcessingService = applicantProcessingService;
        this.evaluationScoreRepository = evaluationScoreRepository;
        this.evaluationDetailRepository = evaluationDetailRepository;
        this.resumeRetrieverRepository = resumeRetrieverRepository;
        this.evaluationRepository = evaluationRepository;
    }

    @Transactional
    public void resumePdfPipe(Long recruitmentId) {
        // 지원자 목록 조회
        List<Applicant> applicants = applicantRepository.findByRecruitmentId(recruitmentId);

        // PDF 정보를 담은 DTO 리스트 생성 (예: 파일 저장 후 얻은 파일 이름 사용)
        List<PdfInfoDTO> pdfInfoDTOList = applicants.stream().map(applicant ->
                PdfInfoDTO.builder()
                        .applicantId(applicant.getId())
                        .pdfName(applicant.getFileName())
                        .build()
        ).collect(Collectors.toList());

        // PdfInfoListRequestDTO 생성 후, InsertResume API 호출 (동기 처리)
        PdfInfoListRequestDTO pdfInfoListRequestDTO = PdfInfoListRequestDTO.builder()
                .pdfInfoList(pdfInfoDTOList)
                .build();
        apiService.callInsertResumeApi(pdfInfoListRequestDTO);

        // 각 지원자별 요약 API 호출 및 업데이트를 비동기로 처리
        List<CompletableFuture<Void>> futures = applicants.stream()
                .map(applicant -> applicantProcessingService.processApplicant(applicant))
                .collect(Collectors.toList());

        // 모든 비동기 작업이 완료될 때까지 대기 (필요에 따라 timeout 등 추가 고려)
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    @Async
    public CompletableFuture<Void> resumePdfPipeAsync(Long recruitmentId) {
        resumePdfPipe(recruitmentId);
        return CompletableFuture.completedFuture(null);
    }


    // score
    @Transactional
    public void scorePipe(Long recruitmentId) {
        // 지원자 목록 조회
        List<Applicant> applicants = applicantRepository.findByRecruitmentId(recruitmentId);
        Recruitment recruitment = applicants.get(0).getRecruitment();
        List<Evaluation> evaluations = recruitment.getEvaluationList();
        List<EvaluationScore> savedEvaluationScoreList = new ArrayList<>();

        for (Applicant applicant : applicants) {
            for (Evaluation evaluation : evaluations) {
                EvaluationDetail evaluationDetail = EvaluationDetail.builder()
                        .build();
                EvaluationDetail savedEvaluationDetail = evaluationDetailRepository.save(evaluationDetail);

                EvaluationScore evaluationScore = EvaluationScore.builder()
                        .evaluation(evaluation)
                        .applicant(applicant)
                        .evaluationDetail(savedEvaluationDetail)
                        .build();

                EvaluationScore savedEvaluationScore = evaluationScoreRepository.save(evaluationScore);
                savedEvaluationScoreList.add(savedEvaluationScore);
            }
        }

        for (EvaluationScore e : savedEvaluationScoreList) {
            ScoreRequestDTO scoreRequestDTO = ScoreRequestDTO.builder()
                    .applicantId(e.getApplicant().getId())
                    .evalItem(e.getEvaluation().getItem())
                    .evalItemContent(e.getEvaluation().getDetail())
                    .job(recruitment.getJob())
                    .build();

            ApiResponseDTO<ScoreResponseDTO> returnDto = apiService.callScoreApi(scoreRequestDTO);
            ScoreResponseDTO dto = returnDto.getItem();

            EvaluationScore es = EvaluationScore.builder()
                    .id(e.getId())
                    .score(dto.getScore())
                    .applicant(e.getApplicant())
                    .evaluation(e.getEvaluation())
                    .evaluationDetail(e.getEvaluationDetail())
                    .build();
            EvaluationScore se = evaluationScoreRepository.save(es);

            EvaluationDetail ed = EvaluationDetail.builder()
                    .id(se.getId())
                    .summary(dto.getReason())
                    .build();
            EvaluationDetail sed = evaluationDetailRepository.save(ed);

            for(String s : dto.getChunk()){
                ResumeRetriever rr = ResumeRetriever.builder()
                        .chunk(s)
                        .applicant(e.getApplicant())
                        .build();
                resumeRetrieverRepository.save(rr);
            }
        }
    }

    @Transactional
    public void scorePipe2(Long recruitmentId) {
        // 지원자 목록 조회
        List<Applicant> applicants = applicantRepository.findByRecruitmentId(recruitmentId);
        Recruitment recruitment = applicants.get(0).getRecruitment();
        List<Evaluation> evaluations = evaluationRepository.findByRecruitment_id(recruitmentId);
        List<EvaluationScore> savedEvaluationScoreList = new ArrayList<>();

        for (Applicant applicant : applicants) {
            for (Evaluation evaluation : evaluations) {
                EvaluationDetail evaluationDetail = EvaluationDetail.builder()
                        .build();
                EvaluationDetail savedEvaluationDetail = evaluationDetailRepository.save(evaluationDetail);

                EvaluationScore evaluationScore = EvaluationScore.builder()
                        .evaluation(evaluation)
                        .applicant(applicant)
                        .evaluationDetail(savedEvaluationDetail)
                        .build();
                EvaluationScore savedEvaluationScore = evaluationScoreRepository.save(evaluationScore);
                savedEvaluationScoreList.add(savedEvaluationScore);
            }
        }

         //각 지원자별 요약 API 호출 및 업데이트를 비동기로 처리
        List<CompletableFuture<Void>> futures = savedEvaluationScoreList.stream()
                .map( se -> applicantProcessingService.processEvaluationScoreAsync(se, recruitment.getJob()))
//                .map( se -> applicantProcessingService.processApplicantScore(se, recruitment.getJob()))
                .collect(Collectors.toList());

         //모든 비동기 작업이 완료될 때까지 대기 (필요에 따라 timeout 등 추가 고려)
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

    }

    @Async
    public CompletableFuture<Void> scorePipeAsync2(Long recruitmentId) {
        self.scorePipe2(recruitmentId);
        return CompletableFuture.completedFuture(null);
    }

}

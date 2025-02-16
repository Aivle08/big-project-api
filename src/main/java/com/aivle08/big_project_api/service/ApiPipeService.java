package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.constants.ProcessingStatus;
import com.aivle08.big_project_api.dto.api.request.*;
import com.aivle08.big_project_api.dto.api.response.ApiResponseDTO;
import com.aivle08.big_project_api.dto.api.response.QuestionResponseDTO;
import com.aivle08.big_project_api.dto.api.response.ScoreResponseDTO;
import com.aivle08.big_project_api.dto.response.QuestionListResponseDTO;
import com.aivle08.big_project_api.model.*;
import com.aivle08.big_project_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final RecruitmentRepository recruitmentRepository;

    public ApiPipeService(ApplicantRepository applicantRepository, ApiService apiService, ApplicantProcessingService applicantProcessingService, EvaluationScoreRepository evaluationScoreRepository, EvaluationDetailRepository evaluationDetailRepository, ResumeRetrieverRepository resumeRetrieverRepository, EvaluationRepository evaluationRepository, RecruitmentRepository recruitmentRepository) {
        this.applicantRepository = applicantRepository;
        this.apiService = apiService;
        this.applicantProcessingService = applicantProcessingService;
        this.evaluationScoreRepository = evaluationScoreRepository;
        this.evaluationDetailRepository = evaluationDetailRepository;
        this.resumeRetrieverRepository = resumeRetrieverRepository;
        this.evaluationRepository = evaluationRepository;
        this.recruitmentRepository = recruitmentRepository;
    }

    @Transactional
    public void resumePdfPipe(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElse(null);
        recruitment.updateProcessingStatus(ProcessingStatus.IN_PROGRESS);
        recruitmentRepository.save(recruitment);

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

            for (String s : dto.getChunk()) {
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
        List<Evaluation> evaluations = evaluationRepository.findByRecruitment_id(recruitmentId);
        List<EvaluationScore> savedEvaluationScoreList = new ArrayList<>();
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElse(null);
        recruitment.updateScoreProcessingStatus(ProcessingStatus.IN_PROGRESS);
        recruitmentRepository.save(recruitment);

        if (applicants.get(0).getEvaluationScoreList().size() == 0) {
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
        }

        //각 지원자별 요약 API 호출 및 업데이트를 비동기로 처리
        List<CompletableFuture<Void>> futures = savedEvaluationScoreList.stream()
                .map(se -> applicantProcessingService.processEvaluationScoreAsync(se, recruitment.getJob()))
//                .map( se -> applicantProcessingService.processApplicantScore(se, recruitment.getJob()))
                .collect(Collectors.toList());

        Recruitment recruitment2 = recruitmentRepository.findById(recruitmentId).orElse(null);
        recruitment2.updateScoreProcessingStatus(ProcessingStatus.COMPLETED);
        recruitmentRepository.save(recruitment);

        //모든 비동기 작업이 완료될 때까지 대기 (필요에 따라 timeout 등 추가 고려)
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

    }

    @Async
    public CompletableFuture<Void> scorePipeAsync2(Long recruitmentId) {
        self.scorePipe2(recruitmentId);
        return CompletableFuture.completedFuture(null);
    }

    public List<QuestionListResponseDTO> questionPipe(Long applicantId) {
        Applicant applicant = applicantRepository.findById(applicantId).get();
        String job = applicant.getRecruitment().getJob();
        Long companyId = applicant.getRecruitment().getId();
        List<Evaluation> evaluations = evaluationRepository.findByRecruitment_id(applicant.getRecruitment().getId());

        // 직무 중심 질문 생성
        QuestionRequestDTO techQuestionRequestDTO = QuestionRequestDTO.builder()
                .job(job)
                .companyId(companyId)
                .applicantId(applicantId)
                .build();
        QuestionResponseDTO techQuestionResponseDTO = apiService.callQuestionApi(techQuestionRequestDTO, "tech").getItem();
        QuestionListResponseDTO techQuestionListResponseDTO;
        if (techQuestionResponseDTO.getQuestion() == null) {
            techQuestionListResponseDTO = QuestionListResponseDTO.builder()
                    .title("직무")
                    .finalQuestion(List.of("재귀한도 초과로 인한 질문생성 불가"))
                    .build();
        } else {
            techQuestionListResponseDTO = QuestionListResponseDTO.builder()
                    .title("직무")
                    .chunk(techQuestionResponseDTO.getChunk())
                    .finalQuestion(techQuestionResponseDTO.getQuestion().getFinalQuestion())
                    .build();
        }


        // 경험 중심 질문 생성
        Optional<String> experienceDetail = evaluations.stream()
                .filter(evaluation -> "채용 공고".equals(evaluation.getItem()))
                .map(Evaluation::getDetail)
                .findFirst();
        String experienceEvaluation = experienceDetail.orElse(null);

        QuestionRequestDTO experienceQuestionRequestDTO = QuestionRequestDTO.builder()
                .job(job)
                .companyId(companyId)
                .applicantId(applicantId)
                .evaluation(experienceEvaluation)
                .build();
        QuestionResponseDTO experienceQuestionResponseDTO = apiService.callQuestionApi(experienceQuestionRequestDTO, "experience").getItem();
        QuestionListResponseDTO experienceQuestionListResponseDTO;
        if (experienceQuestionResponseDTO.getQuestion() == null) {
            experienceQuestionListResponseDTO = QuestionListResponseDTO.builder()
                    .title("직무")
                    .finalQuestion(List.of("재귀한도 초과로 인한 질문생성 불가"))
                    .build();
        } else {
            experienceQuestionListResponseDTO = QuestionListResponseDTO.builder()
                    .title("직무")
                    .chunk(experienceQuestionResponseDTO.getChunk())
                    .finalQuestion(experienceQuestionResponseDTO.getQuestion().getFinalQuestion())
                    .build();
        }

        // 일 중심 질문 생성
        Optional<String> workDetail = evaluations.stream()
                .filter(evaluation -> "채용 공고".equals(evaluation.getItem()))
                .map(Evaluation::getDetail)
                .findFirst();
        String workEvaluation = workDetail.orElse(null);

        QuestionRequestDTO workQuestionRequestDTO = QuestionRequestDTO.builder()
                .job(job)
                .companyId(companyId)
                .applicantId(applicantId)
                .evaluation(workEvaluation)
                .build();
        QuestionResponseDTO workQuestionResponseDTO = apiService.callQuestionApi(workQuestionRequestDTO, "work").getItem();
        QuestionListResponseDTO workQuestionListResponseDTO;
        if (workQuestionResponseDTO.getQuestion() == null) {
            workQuestionListResponseDTO = QuestionListResponseDTO.builder()
                    .title("일")
                    .finalQuestion(List.of("재귀한도 초과로 인한 질문생성 불가"))
                    .build();
        } else {
            workQuestionListResponseDTO = QuestionListResponseDTO.builder()
                    .title("일")
                    .chunk(workQuestionResponseDTO.getChunk())
                    .finalQuestion(workQuestionResponseDTO.getQuestion().getFinalQuestion())
                    .build();
        }

        List<QuestionListResponseDTO> questionListResponseDTOList = List.of(techQuestionListResponseDTO, experienceQuestionListResponseDTO, workQuestionListResponseDTO);

        return questionListResponseDTOList;
    }

    public void inserdetailPipe(Long recruitmentId) {
        List<Evaluation> evaluations = evaluationRepository.findByRecruitment_id(recruitmentId);
        List<String> evaluationDetailList = evaluations.stream().map(Evaluation::getDetail).collect(Collectors.toList());

        RecruitmentInputDTO recruitmentInputDTO = RecruitmentInputDTO.builder()
                .recruitmentId(recruitmentId)
                .detailList(evaluationDetailList)
                .build();

        ApiResponseDTO<Void> responseDTO = apiService.callInserDetail(recruitmentInputDTO);
    }


}

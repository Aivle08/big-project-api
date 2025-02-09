package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.api.request.PdfInfoDTO;
import com.aivle08.big_project_api.dto.api.request.PdfInfoListRequestDTO;
import com.aivle08.big_project_api.dto.api.request.QuestionRequestDTO;
import com.aivle08.big_project_api.dto.api.request.ScoreRequestDTO;
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

        if (applicants.get(0).getEvaluationScoreList() == null) {
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

    public List<QuestionListResponseDTO> questionPipe(Long applicantId) {
        Applicant applicant = applicantRepository.findById(applicantId).get();
        String job = applicant.getRecruitment().getJob();
        List<Evaluation> evaluations = evaluationRepository.findByRecruitment_id(applicant.getRecruitment().getId());

        // 직무 중심 질문 생성
        QuestionRequestDTO techQuestionRequestDTO = QuestionRequestDTO.builder()
                .job(job)
                .companyId(1L)
                .applicantId(101L)
//                .evaluation()
                .build();
        QuestionResponseDTO techQuestionResponseDTO = apiService.callQuestionApi(techQuestionRequestDTO, "tech").getItem();
        QuestionListResponseDTO techQuestionListResponseDTO = QuestionListResponseDTO.builder()
                .title("직무")
                .chunk(techQuestionResponseDTO.getChunk())
                .finalQuestion(techQuestionResponseDTO.getQuestion().getFinalQuestion())
                .build();

        // 경험 중심 질문 생성
        Optional<String> experienceDetail = evaluations.stream()
                .filter(evaluation -> "실력".equals(evaluation.getItem()))
                .map(Evaluation::getDetail)
                .findFirst();
        String experienceEvaluation = experienceDetail.orElse(null);

        QuestionRequestDTO experienceQuestionRequestDTO = QuestionRequestDTO.builder()
                .job(job)
                .companyId(1L)
                .applicantId(101L)
//                .evaluation(experienceEvaluation)
                .evaluation("\n    5. 공고\n    수행업무\n    • 대기업/중견기업 고객 대상 ICT 관련 영업활동\n    o Industry 별 ICT, AI, DX 시장정보 수집\n    o 고객사 니즈 파악을 통한 세일즈 전략 수립\n    o 영업 기회 발굴 및 매출화/이익 관리\n    o 고객 디지털 혁신 니즈를 선제안을 통한 사업화 추진\n    o 고객 Care 등 고객 만족 활동 지원\n    o 다양한 내외부 관계자들과의 소통, 이해관계 조율 등의 제반업무\n    수행\n    • 공공, 국방, 금융고객 대상 B2B 세일즈 및 고객 관리\n    o 공공, 국방, 금융 고객 신규 시장 및 영업 기회 발굴, 고객 Care\n    활동\n    o 관련 분야 시장 정보 수집을 통한 세일즈 전략 수립, 솔루션/서비스\n    제안\n    o 영업 활동을 통한 사업목표 달성 및 매출/이익 관리\n    우대요건\n    • ICT 기술 전반에 대한 지식 보유 및 시장 분석 역량\n    • 비즈니스 Writing& Speeching 역량\n    • 원활한 커뮤니케이션 능력\n    KT 의 IT 영업 직무는 대기업/중견기업, 공공, 국방, 금융 고객을 대상으로 ICT\n    관련 솔루션과 서비스를 제공하며, 고객의 디지털 혁신을 선도하는 역할을\n    수행합니다. 신입 지원자는 해당 직무의 특성을 반영하여 기술적 이해, 영업\n    전략 수립, 고객 니즈 파악 및 매출화 등을 잘 수행할 수 있는 역량을 갖춰야\n    합니다.\n    기본 요구 사항\n    1. 학력 및 전공\n    o 최소 학사 학위 보유 (ICT 관련 전공 우대: 컴퓨터공학, 전자공학,\n    경영학 등)\n    o 전공과 관계없이 ICT 기술 및 디지털 혁신에 관심이 있는 지원자\n    우대\n    2. 기본 기술적 이해\n    o ICT(정보통신기술), AI, DX(디지털 트랜스포메이션)에 대한 기본적인\n    이해\n    o 클라우드, 빅데이터, IoT, AI 등 최신 IT 기술에 대한 관심 및\n    기본적인 지식 보유\n    3. 영업 전략 및 고객 니즈 분석\n    o 고객의 요구를 파악하고 이에 맞는 세일즈 전략 수립\n    경험(대외활동, 프로젝트, 인턴 경험 등)\n    o 고객 만족을 위해 영업 활동을 통한 문제 해결 및 서비스 개선 경험\n    우대 사항\n    1. ICT 및 관련 산업 지식\n    o ICT 시장에 대한 이해(산업별 시장 정보 수집 경험, 트렌드 분석\n    등)\n    o 디지털 혁신 관련 활동 또는 경험 (AI, 클라우드, 데이터 분석 등\n    활용 경험)\n    o B2B 세일즈 경험(공공, 금융, 국방 분야 관련 경험 우대)\n    2. 영업 및 커뮤니케이션 능력\n    o 비즈니스 Writing(제안서 작성) 및 Speeching(발표 및\n    프레젠테이션) 역량 보유\n    o 고객 니즈 분석 및 그에 맞는 솔루션 제안 경험\n    o 팀워크 및 협업 경험이 있는 경우 우대 (팀 프로젝트, 대외활동 등)\n    3. 고객 관리 및 사업화 경험\n    o 고객 Care 활동을 통해 고객 만족을 증대시킨 경험\n    o 영업 기회 발굴 및 매출화/이익 관리 경험(학업 또는\n    대외활동에서의 경험 포함 가능)\n    o 사업 목표 달성 및 고객과의 장기적 관계 구축을 위한 노하우 보유\n    4. 전문적 자격증\n    o IT 관련 자격증 (정보처리기사, AWS, Azure 등)\n    o 영업 관련 자격증(CRM, 마케팅 관련 자격증) 보유자 우대\n    경력/대외활동 경험\n    • 인턴 경험: IT 영업, ICT 관련 기업, 공공/금융/국방 관련 기관에서의 인턴\n    경험.\n    • 프로젝트 경험: IT 관련 프로젝트(예: 클라우드 도입, 데이터 분석 시스템\n    구축 등)에서 고객 니즈 파악, 솔루션 제안, 매출화 등 직무와 연계된 활동\n    경험.\n    • 대외활동 경험: IT 관련 경진대회, 공모전, 세미나 참여 등 ICT 관련\n    산업에 대한 이해를 증명할 수 있는 경험\n                            \n    ")
                .build();
        QuestionResponseDTO experienceQuestionResponseDTO = apiService.callQuestionApi(experienceQuestionRequestDTO, "experience").getItem();
        QuestionListResponseDTO experienceQuestionListResponseDTO = QuestionListResponseDTO.builder()
                .title("경험")
                .chunk(experienceQuestionResponseDTO.getChunk())
                .finalQuestion(experienceQuestionResponseDTO.getQuestion().getFinalQuestion())
                .build();

        // 일 중심 질문 생성
        Optional<String> workDetail = evaluations.stream()
                .filter(evaluation -> "역량".equals(evaluation.getItem()))
                .map(Evaluation::getDetail)
                .findFirst();
        String workEvaluation = workDetail.orElse(null);

        QuestionRequestDTO workQuestionRequestDTO = QuestionRequestDTO.builder()
                .job(job)
                .companyId(1L)
                .applicantId(101L)
//                .evaluation(workEvaluation)
                .evaluation("\n    5. 공고\n    수행업무\n    • 대기업/중견기업 고객 대상 ICT 관련 영업활동\n    o Industry 별 ICT, AI, DX 시장정보 수집\n    o 고객사 니즈 파악을 통한 세일즈 전략 수립\n    o 영업 기회 발굴 및 매출화/이익 관리\n    o 고객 디지털 혁신 니즈를 선제안을 통한 사업화 추진\n    o 고객 Care 등 고객 만족 활동 지원\n    o 다양한 내외부 관계자들과의 소통, 이해관계 조율 등의 제반업무\n    수행\n    • 공공, 국방, 금융고객 대상 B2B 세일즈 및 고객 관리\n    o 공공, 국방, 금융 고객 신규 시장 및 영업 기회 발굴, 고객 Care\n    활동\n    o 관련 분야 시장 정보 수집을 통한 세일즈 전략 수립, 솔루션/서비스\n    제안\n    o 영업 활동을 통한 사업목표 달성 및 매출/이익 관리\n    우대요건\n    • ICT 기술 전반에 대한 지식 보유 및 시장 분석 역량\n    • 비즈니스 Writing& Speeching 역량\n    • 원활한 커뮤니케이션 능력\n    KT 의 IT 영업 직무는 대기업/중견기업, 공공, 국방, 금융 고객을 대상으로 ICT\n    관련 솔루션과 서비스를 제공하며, 고객의 디지털 혁신을 선도하는 역할을\n    수행합니다. 신입 지원자는 해당 직무의 특성을 반영하여 기술적 이해, 영업\n    전략 수립, 고객 니즈 파악 및 매출화 등을 잘 수행할 수 있는 역량을 갖춰야\n    합니다.\n    기본 요구 사항\n    1. 학력 및 전공\n    o 최소 학사 학위 보유 (ICT 관련 전공 우대: 컴퓨터공학, 전자공학,\n    경영학 등)\n    o 전공과 관계없이 ICT 기술 및 디지털 혁신에 관심이 있는 지원자\n    우대\n    2. 기본 기술적 이해\n    o ICT(정보통신기술), AI, DX(디지털 트랜스포메이션)에 대한 기본적인\n    이해\n    o 클라우드, 빅데이터, IoT, AI 등 최신 IT 기술에 대한 관심 및\n    기본적인 지식 보유\n    3. 영업 전략 및 고객 니즈 분석\n    o 고객의 요구를 파악하고 이에 맞는 세일즈 전략 수립\n    경험(대외활동, 프로젝트, 인턴 경험 등)\n    o 고객 만족을 위해 영업 활동을 통한 문제 해결 및 서비스 개선 경험\n    우대 사항\n    1. ICT 및 관련 산업 지식\n    o ICT 시장에 대한 이해(산업별 시장 정보 수집 경험, 트렌드 분석\n    등)\n    o 디지털 혁신 관련 활동 또는 경험 (AI, 클라우드, 데이터 분석 등\n    활용 경험)\n    o B2B 세일즈 경험(공공, 금융, 국방 분야 관련 경험 우대)\n    2. 영업 및 커뮤니케이션 능력\n    o 비즈니스 Writing(제안서 작성) 및 Speeching(발표 및\n    프레젠테이션) 역량 보유\n    o 고객 니즈 분석 및 그에 맞는 솔루션 제안 경험\n    o 팀워크 및 협업 경험이 있는 경우 우대 (팀 프로젝트, 대외활동 등)\n    3. 고객 관리 및 사업화 경험\n    o 고객 Care 활동을 통해 고객 만족을 증대시킨 경험\n    o 영업 기회 발굴 및 매출화/이익 관리 경험(학업 또는\n    대외활동에서의 경험 포함 가능)\n    o 사업 목표 달성 및 고객과의 장기적 관계 구축을 위한 노하우 보유\n    4. 전문적 자격증\n    o IT 관련 자격증 (정보처리기사, AWS, Azure 등)\n    o 영업 관련 자격증(CRM, 마케팅 관련 자격증) 보유자 우대\n    경력/대외활동 경험\n    • 인턴 경험: IT 영업, ICT 관련 기업, 공공/금융/국방 관련 기관에서의 인턴\n    경험.\n    • 프로젝트 경험: IT 관련 프로젝트(예: 클라우드 도입, 데이터 분석 시스템\n    구축 등)에서 고객 니즈 파악, 솔루션 제안, 매출화 등 직무와 연계된 활동\n    경험.\n    • 대외활동 경험: IT 관련 경진대회, 공모전, 세미나 참여 등 ICT 관련\n    산업에 대한 이해를 증명할 수 있는 경험\n                            \n    ")
                .build();
        QuestionResponseDTO workQuestionResponseDTO = apiService.callQuestionApi(workQuestionRequestDTO, "work").getItem();
        QuestionListResponseDTO workQuestionListResponseDTO = QuestionListResponseDTO.builder()
                .title("일")
                .chunk(workQuestionResponseDTO.getChunk())
                .finalQuestion(workQuestionResponseDTO.getQuestion().getFinalQuestion())
                .build();

        List<QuestionListResponseDTO> questionListResponseDTOList = List.of(techQuestionListResponseDTO, experienceQuestionListResponseDTO, workQuestionListResponseDTO);

        return questionListResponseDTOList;
    }


}

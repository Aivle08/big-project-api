package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.api.request.PdfInfoDTO;
import com.aivle08.big_project_api.dto.api.request.PdfInfoListRequestDTO;
import com.aivle08.big_project_api.model.Applicant;
import com.aivle08.big_project_api.repository.ApplicantRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ApiPipeService {

    private final ApplicantRepository applicantRepository;
    private final ApiService apiService;
    private final ApplicantProcessingService applicantProcessingService;

    public ApiPipeService(ApplicantRepository applicantRepository, ApiService apiService, ApplicantProcessingService applicantProcessingService) {
        this.applicantRepository = applicantRepository;
        this.apiService = apiService;
        this.applicantProcessingService = applicantProcessingService;
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
}

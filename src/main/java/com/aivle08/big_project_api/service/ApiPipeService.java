package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.api.request.PdfInfoDTO;
import com.aivle08.big_project_api.dto.api.request.PdfInfoListRequestDTO;
import com.aivle08.big_project_api.dto.api.response.ApiResponseDTO;
import com.aivle08.big_project_api.dto.api.response.ExtractionResponseDTO;
import com.aivle08.big_project_api.model.Applicant;
import com.aivle08.big_project_api.repository.ApplicantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApiPipeService {

    private final ApplicantRepository applicantRepository;
    private final ApiService apiService;

    public ApiPipeService(ApplicantRepository applicantRepository, ApiService apiService) {
        this.applicantRepository = applicantRepository;
        this.apiService = apiService;
    }

    @Transactional
    public void resumePdfPipe(Long recruitmentId) {
        List<Applicant> applicants = applicantRepository.findByRecruitmentId(recruitmentId);
        // pdf 저장
        // 요약 호출
        // 저장

        List<PdfInfoDTO> pdfInfoDTOList = applicants.stream().map(applicant -> {
            PdfInfoDTO pdfInfoDTO = PdfInfoDTO.builder()
                    .applicantId(applicant.getId())
                    .pdfName(applicant.getFileName())
                    .build();
            return pdfInfoDTO;
        }).toList();

        PdfInfoListRequestDTO pdfInfoListRequestDTO = PdfInfoListRequestDTO.builder()
                .pdfInfoList(pdfInfoDTOList).build();
        apiService.callInsertResumeApi(pdfInfoListRequestDTO);

        applicants.stream().forEach(applicant -> {
            Long applicantId = applicant.getId();
            ApiResponseDTO apiResponseDTO = apiService.callSummaryExtractionApi(applicantId);

            ObjectMapper objectMapper = new ObjectMapper();
            ExtractionResponseDTO dto = objectMapper.convertValue(apiResponseDTO.getItem(), ExtractionResponseDTO.class);

            Applicant updateApplicant = Applicant.builder()
                    .id(applicant.getId())
                    .name(dto.getName())
                    .contact(dto.getPhone())
                    .email(dto.getEmail())
                    .resumeResult(false)
                    .resumeSummary(dto.getElseSummary())
                    .recruitment(applicant.getRecruitment())
                    .fileName(applicant.getFileName())
                    .build();

            applicantRepository.save(updateApplicant);
        });
    }
}

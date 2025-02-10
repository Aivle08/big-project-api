package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.request.ApplicantRequestDTO;
import com.aivle08.big_project_api.dto.response.ApplicantResponseDTO;
import com.aivle08.big_project_api.dto.response.FileUploadResponseDTO;
import com.aivle08.big_project_api.service.ApplicantService;
import com.aivle08.big_project_api.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// TODO: 예외처리 추가 및 반환 값 지정
@RestController
@RequestMapping("/api/v1/recruitment/{id}")
@Tag(name = "Applicant API", description = "지원자 조회 API")
public class ApplicantController {
    private final ApplicantService applicantService;
    private final S3Service s3Service;

    public ApplicantController(ApplicantService applicantService, S3Service s3Service) {
        this.applicantService = applicantService;
        this.s3Service = s3Service;
    }

    @GetMapping("/applicant")
    @Operation(summary = "지원자 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지원자 리스트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<List<ApplicantResponseDTO>> getApplicantListByRecruitmentId(@PathVariable Long id) {
        List<ApplicantResponseDTO> applicantListInputDTO = applicantService.getApplicantListByRecruitmentId(id);

        return ResponseEntity.ok().body(applicantListInputDTO);
    }

    @PostMapping
    @Operation(summary = "지원자 리스트 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지원자 리스트 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<ApplicantRequestDTO> createRecruitment(@RequestBody ApplicantRequestDTO applicantRequestDTO, @PathVariable Long id) {
        applicantService.createApplicant(applicantRequestDTO, id);
        return ResponseEntity.ok()
                .body(applicantRequestDTO);
    }

    @Operation(summary = "PDF 파일 업로드", description = "여러 개의 PDF 파일을 업로드합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "파일 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 파일 형식"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping(value = "/upload-resume-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponseDTO> uploadResumePDF(@PathVariable Long id, @RequestPart("files") List<MultipartFile> files) {
        if (files.isEmpty()) {
            FileUploadResponseDTO response = new FileUploadResponseDTO(
                    "badRequest",
                    "files uploaded badRequest.",
                    null
            );
            return ResponseEntity.badRequest().body(response);
        }
        List<String> uploadedFileNames = s3Service.storeFiles(files, id);
        FileUploadResponseDTO response = new FileUploadResponseDTO(
                "success",
                uploadedFileNames.size() + " files uploaded successfully.",
                uploadedFileNames
        );
        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(summary = "PDF 파일 불러오기", description = "지원자 아이디에 해당하는 이력서 PDF를 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "파일 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping(value = "/applicant/{applicant-id}/pdf")
    public ResponseEntity<byte[]> getResumePDF(@PathVariable Long id,
                                               @PathVariable(name = "applicant-id") Long applicantId) {
        ApplicantResponseDTO applicantResponse = applicantService.getApplicantById(applicantId);
        if (applicantResponse == null || applicantResponse.getFileName() == null) {
            return ResponseEntity.badRequest().build();
        }
        String fileName = applicantResponse.getFileName();
        byte[] fileContent = s3Service.getPdfFile(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(fileContent);
    }
}

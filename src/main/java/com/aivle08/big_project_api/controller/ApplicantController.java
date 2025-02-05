package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.request.ApplicantRequestDTO;
import com.aivle08.big_project_api.dto.response.ApplicantResponseDTO;
import com.aivle08.big_project_api.dto.response.FileUploadResponseDTO;
import com.aivle08.big_project_api.service.ApplicantService;
import com.aivle08.big_project_api.service.FileStorageService;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

// TODO: 예외처리 추가 및 반환 값 지정
@RestController
@RequestMapping("/api/v1/recruitment/{id}")
@Tag(name = "Applicant API", description = "지원자 조회 API")
public class ApplicantController {
    private final ApplicantService applicantService;
    private final FileStorageService fileStorageService;
    private final S3Service s3Service;

    public ApplicantController(ApplicantService applicantService, FileStorageService fileStorageService, S3Service s3Service) {
        this.applicantService = applicantService;
        this.fileStorageService = fileStorageService;
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

    @Operation(summary = "파일 다운로드", description = "리쿠르먼트 ID와 파일 이름으로 파일을 다운로드합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "파일 다운로드 성공"),
            @ApiResponse(responseCode = "400", description = "파일을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping(value = "/applicant/file/{fileName}")
//    @GetMapping(value = "/applicant/{applicantId}/resume-pdf")
    public ResponseEntity<?> getFile(@PathVariable Long id, @PathVariable String fileName) {
        try {
            File file = fileStorageService.getFile(id.toString(), fileName);

            byte[] fileContent = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                    .body(fileContent);
        } catch (IOException ex) {
            return ResponseEntity.status(500).body("Failed to read file: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(400).body("Error: " + ex.getMessage());
        }
    }

    @Operation(summary = "PDF 파일 업로드", description = "지원자의 PDF 파일을 S3 버킷에 업로드합니다. 파일명은 '공고ID_지원자ID.pdf' 형식으로 변경됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "파일 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 파일 형식 또는 파일이 없을 때"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping(value = "/applicant/{applicantId}/upload-resume-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponseDTO> uploadResumePDF(
            @PathVariable Long id,
            @PathVariable Long applicantId,
            @RequestPart("files") List<MultipartFile> files) {

        // 파일이 없으면 400 Bad Request 반환
        if (files == null || files.isEmpty()) {
            FileUploadResponseDTO response = new FileUploadResponseDTO(
                    "badRequest",
                    "No files uploaded.",
                    null
            );
            return ResponseEntity.badRequest().body(response);
        }

        // 여러 파일이 들어오더라도 여기서는 첫 번째 파일만 업로드하는 예시입니다.
        MultipartFile file = files.get(0);

        String s3Url = s3Service.uploadOneFile(file, id);

//        // 파일 확장자 체크 (pdf만 허용)
//        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
//            FileUploadResponseDTO response = new FileUploadResponseDTO(
//                    "badRequest",
//                    "Only PDF files are allowed.",
//                    null
//            );
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        // 새로운 파일명: recruitmentId_applicantId.pdf
//        String newFileName = id + "_" + applicantId + ".pdf";
//
//        // S3에 파일 업로드
//        String s3Url = s3Service.uploadFile(file, newFileName);

        // 응답 DTO 생성 (파일 업로드 결과 및 URL)
        FileUploadResponseDTO response = new FileUploadResponseDTO(
                "success",
                "File uploaded successfully.",
                List.of(s3Url)
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(response);
    }

}

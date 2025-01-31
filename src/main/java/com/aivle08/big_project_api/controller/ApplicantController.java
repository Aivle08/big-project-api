package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.request.ApplicantRequestDTO;
import com.aivle08.big_project_api.service.ApplicantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: 예외처리 추가 및 반환 값 지정

@RestController
@RequestMapping("/api/v1/recruitment/{id}")
@Tag(name = "Applicant API", description = "지원자 조회 API")
public class ApplicantController {
    private final ApplicantService applicantService;

    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @GetMapping("/applicant")
    @Operation(summary = "지원자 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지원자 리스트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<List<ApplicantRequestDTO>> getApplicantsByRecruitmentId(@PathVariable Long id) {
        List<ApplicantRequestDTO> applicantsInputDTO = applicantService.getApplicantsByRecruitmentId(id);

        return ResponseEntity.ok().body(applicantsInputDTO);
    }

    @PostMapping
    @Operation(summary = "지원자 리스트 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지원자 리스트 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<ApplicantRequestDTO> postRecruitment(@RequestBody ApplicantRequestDTO applicantRequestDTO, @PathVariable Long id) {
        applicantService.applicantCreate(applicantRequestDTO, id);
        return ResponseEntity.ok()
                .body(applicantRequestDTO);
    }
}

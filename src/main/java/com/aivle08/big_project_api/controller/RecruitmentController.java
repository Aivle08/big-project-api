package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.constants.ProcessingStatus;
import com.aivle08.big_project_api.dto.request.RecruitmentRequestDTO;
import com.aivle08.big_project_api.dto.response.RecruitmentResponseDTO;
import com.aivle08.big_project_api.repository.RecruitmentRepository;
import com.aivle08.big_project_api.service.ApiPipeService;
import com.aivle08.big_project_api.service.RecruitmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruitment")
@Tag(name = "Recruitment API", description = "채용 공고 조회 API")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;
    private final RecruitmentRepository recruitmentRepository;
    private final ApiPipeService apiPipeService;

    public RecruitmentController(RecruitmentService recruitmentService, RecruitmentRepository recruitmentRepository, ApiPipeService apiPipeService) {
        this.recruitmentService = recruitmentService;
        this.recruitmentRepository = recruitmentRepository;
        this.apiPipeService = apiPipeService;
    }

    @GetMapping
    @Operation(summary = "채용 공고 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공고 리스트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<List<RecruitmentResponseDTO>> getRecruitmentList() {
        return ResponseEntity.ok()
                .body(recruitmentService.getRecruitmentList());
    }

    @PostMapping
    @Operation(summary = "채용 공고 리스트 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공고 리스트 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<RecruitmentResponseDTO> createRecruitment(@RequestBody RecruitmentRequestDTO recruitmentRequestDTO) {
        RecruitmentResponseDTO recruitmentResponseDTO = recruitmentService.createRecruitment(recruitmentRequestDTO);
        apiPipeService.inserdetailPipe(recruitmentResponseDTO.getId());
        return ResponseEntity.ok()
                .body(recruitmentResponseDTO);
    }

    @GetMapping("/{recruitmentId}/status")
    public ResponseEntity<ProcessingStatus> getRecruitmentProcessingStatus(@PathVariable Long recruitmentId) {
        return recruitmentRepository.findById(recruitmentId)
                .map(recruitment -> ResponseEntity.ok(recruitment.getProcessingStatus()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{recruitmentId}/score-status")
    public ResponseEntity<?> getRecruitmentScoreStatus(@PathVariable Long recruitmentId) {
        return recruitmentRepository.findById(recruitmentId)
                .map(recruitment -> {
                    ProcessingStatus status = recruitment.getScoreProcessingStatus();
                    return ResponseEntity.ok(status);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

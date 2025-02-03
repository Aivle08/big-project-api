package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.response.EvaluationResponseDTO;
import com.aivle08.big_project_api.service.ApplicantService;
import com.aivle08.big_project_api.service.EvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruitment/{recruitmentId}")
@CrossOrigin
@Tag(name = "Evaluation API", description = "평가 조회 API")
public class EvaluationController {

    private final ApplicantService applicantService;
    private final EvaluationService evaluationService;

    public EvaluationController(ApplicantService applicantService, EvaluationService evaluationService) {
        this.applicantService = applicantService;
        this.evaluationService = evaluationService;
    }

    @Operation(summary = "지원자 평가조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "400", description = ""),
    })
    @GetMapping("applicant/{applicantId}")
    public ResponseEntity<EvaluationResponseDTO> getScoresByApplicantId(@PathVariable Long recruitmentId,
                                                                        @PathVariable Long applicantId) {
        EvaluationResponseDTO scoresByApplicant = evaluationService.getScoresByApplicantIdandRecruitmentId(recruitmentId, applicantId);
        return ResponseEntity.ok(scoresByApplicant);
    }

    @Operation(summary = "모든 지원자 평가조회", description = "true = 합격자 지원자 평가 조회, false = 전체 지원자 평가 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "400", description = ""),
    })
    @GetMapping("/applicants")
    public ResponseEntity<?> getEvaluations(@PathVariable Long recruitmentId,
                                            @RequestParam(name = "passed", defaultValue = "false") boolean passed) {
        if (passed) {
            return ResponseEntity.ok(evaluationService.getPassedApplicants(recruitmentId));
        }
        else {
            return ResponseEntity.ok(evaluationService.getAllApplicantEvaluations(recruitmentId));
        }
    }
}
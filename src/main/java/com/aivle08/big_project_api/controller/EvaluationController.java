package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.request.ApplicantRequestDTO;
import com.aivle08.big_project_api.dto.response.EvaluationResponseDTO;
import com.aivle08.big_project_api.service.EvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruitment/{recruitmentId}")
@CrossOrigin
@Tag(name = "Evaluation API", description = "평가 조회 API")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @Operation(summary = "지원자 평가조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지원자 평가조회 성공"),
            @ApiResponse(responseCode = "400", description = ""),
    })
    @GetMapping("applicant/{applicantId}")
    public ResponseEntity<EvaluationResponseDTO> getScoreListByApplicantIdAndRecruitmentId(@PathVariable Long recruitmentId,
                                                                                           @PathVariable Long applicantId) {
        EvaluationResponseDTO scoresByApplicant = evaluationService.getScoreListByApplicantIdAndRecruitmentId(recruitmentId, applicantId);
        return ResponseEntity.ok(scoresByApplicant);
    }

    @Operation(summary = "모든 지원자 평가조회", description = "true = 합격자 지원자 평가 조회, false = 전체 지원자 평가 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 지원자 평가조회 성공"),
            @ApiResponse(responseCode = "400", description = ""),
    })
    @GetMapping("/applicants")
    public ResponseEntity<List<EvaluationResponseDTO>> getEvaluationList(@PathVariable Long recruitmentId,
                                               @RequestParam(name = "passed", defaultValue = "false") boolean passed) {
        if (passed) {
            return ResponseEntity.ok(evaluationService.getPassedApplicantList(recruitmentId));
        }
        else {
            return ResponseEntity.ok(evaluationService.getApplicantEvaluationList(recruitmentId));
        }
    }

    @Operation(summary = "합격자 리스트 저장", description = "합격자 id를 리스트로 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "합격자 리스트 저장 성공"),
            @ApiResponse(responseCode = "400", description = ""),
    })
    @PostMapping("/applicants/pass")
    public ResponseEntity<List<ApplicantRequestDTO>> getPassedApplicantById(@RequestBody List<Long> applicantIdList, @PathVariable Long recruitmentId) {

        List<ApplicantRequestDTO> applicantRequestDTOS = evaluationService.getPassApplicantById(applicantIdList);
        return ResponseEntity.ok(applicantRequestDTOS);
    }
}
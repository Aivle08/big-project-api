package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.request.ApplicantRequestDTO;
import com.aivle08.big_project_api.dto.response.EvaluationDetailResponseDTO;
import com.aivle08.big_project_api.dto.response.EvaluationResponseDTO;
import com.aivle08.big_project_api.dto.response.RecruitmentResponseDTO;
import com.aivle08.big_project_api.service.ApplicantService;
import com.aivle08.big_project_api.service.EvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/recruitment/{recruitmentId}")
@CrossOrigin
public class EvaluationController {

    private final ApplicantService applicantService;
    private final EvaluationService evaluationService;

    public EvaluationController(ApplicantService applicantService, EvaluationService evaluationService) {
        this.applicantService = applicantService;
        this.evaluationService = evaluationService;
    }

    @GetMapping("applicant/{applicantId}/scores")
    public ResponseEntity<EvaluationResponseDTO> getScoresByApplicantId(@PathVariable Long recruitmentId, @PathVariable Long applicantId) {
        EvaluationResponseDTO scoresByApplicant = evaluationService.getScoresByApplicantIdandRecruitmentId(recruitmentId, applicantId);
        return ResponseEntity.ok(scoresByApplicant);
    }
}

package com.aivle08.big_project_api.controller;


import com.aivle08.big_project_api.dto.request.EvaluationScoreRequestDTO;
import com.aivle08.big_project_api.model.EvaluationScore;
import com.aivle08.big_project_api.service.EvaluationScoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applicant")
@Tag(name = "ApplicantScore API", description = "지원자 점수 저장 API")
public class ApplicantScoreController {

    private final EvaluationScoreService evaluationScoreService;

    public ApplicantScoreController(EvaluationScoreService evaluationScoreService) {
        this.evaluationScoreService = evaluationScoreService;
    }

    @PostMapping("/{applicantId}/scores")
    public ResponseEntity<List<EvaluationScore>> createEvaluationScoreList(
            @RequestBody List<EvaluationScoreRequestDTO> evaluationScores,
            @PathVariable Long applicantId) {
        List<EvaluationScore> savedScores = evaluationScoreService.createEvaluationScoreList(evaluationScores, applicantId);
        return ResponseEntity.ok(savedScores);
    }
}

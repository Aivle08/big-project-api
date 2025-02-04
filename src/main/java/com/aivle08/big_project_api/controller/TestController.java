package com.aivle08.big_project_api.controller;


import com.aivle08.big_project_api.dto.request.EvaluationScoreRequestDTO;
import com.aivle08.big_project_api.model.EvaluationScore;
import com.aivle08.big_project_api.service.EvaluationScoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final EvaluationScoreService evaluationScoreService;

    public TestController(EvaluationScoreService evaluationScoreService) {
        this.evaluationScoreService = evaluationScoreService;
    }

    @PostMapping("/{applicantId}/scores")
    public ResponseEntity<List<EvaluationScore>> saveEvaluationScores(
            @RequestBody List<EvaluationScoreRequestDTO> evaluationScores,
            @PathVariable Long applicantId) {
        List<EvaluationScore> savedScores = evaluationScoreService.createEvaluationScore(evaluationScores, applicantId);
        return ResponseEntity.ok(savedScores);
    }
}

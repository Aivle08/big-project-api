package com.aivle08.big_project_api.controller;


import com.aivle08.big_project_api.model.EvaluationScore;
import com.aivle08.big_project_api.service.EvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final EvaluationService evaluationService;

    public TestController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping("/{applicantId}/scores")
    public ResponseEntity<List<EvaluationScore>> saveEvaluationScores(
            @RequestBody List<EvaluationScore> evaluationScores,
            @PathVariable Long applicantId) {
        List<EvaluationScore> savedScores = evaluationService.createEvaluationScore(evaluationScores, applicantId);
        return ResponseEntity.ok(savedScores);
    }
}

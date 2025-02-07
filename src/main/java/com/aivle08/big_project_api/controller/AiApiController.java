package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.service.ApiPipeService;
import com.aivle08.big_project_api.service.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai-api")
public class AiApiController {
    private final ApiPipeService apiPipeService;
    private final ApiService apiService;

    public AiApiController(ApiPipeService apiPipeService, ApiService apiService) {
        this.apiPipeService = apiPipeService;
        this.apiService = apiService;
    }

    @PostMapping("/{recruitment-id}/resume-pdf")
    public ResponseEntity<String> postResumePipeCall(@PathVariable(name = "recruitment-id") Long recruitmentId) {
        apiPipeService.resumePdfPipe(recruitmentId);
        return ResponseEntity.ok("Resume processing completed.");
    }

    @PostMapping("/{recruitment-id}/resume-pdf-async")
    public ResponseEntity<String> postResumePipeCallAsync(@PathVariable(name = "recruitment-id") Long recruitmentId) {
        apiPipeService.resumePdfPipeAsync(recruitmentId);
        return ResponseEntity.accepted().body("Resume processing started.");
    }

    @PostMapping("/{applicant-id}/question")
    public ResponseEntity<String> postQuestionCall(@PathVariable(name = "applicant-id") Long applicantId) {
        return ResponseEntity.accepted().body("Resume processing started.");
    }

    @PostMapping("/{recruitment-id}/score")
    public ResponseEntity<String> postScoreCall(@PathVariable(name = "recruitment-id") Long recruitmentId) {
        apiPipeService.scorePipe(recruitmentId);
        return ResponseEntity.accepted().body("Resume processing started.");
    }

    @PostMapping("/{recruitment-id}/score-async")
    public ResponseEntity<String> postScoreCallAsync(@PathVariable(name = "recruitment-id") Long recruitmentId) {
        apiPipeService.scorePipeAsync2(recruitmentId);
        return ResponseEntity.accepted().body("Score processing started.");
    }
}

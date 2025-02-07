package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.service.ApiPipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai-api")
public class AiApiController {
    private final ApiPipeService apiPipeService;

    public AiApiController(ApiPipeService apiPipeService) {
        this.apiPipeService = apiPipeService;
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
}

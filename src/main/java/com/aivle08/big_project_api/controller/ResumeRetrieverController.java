package com.aivle08.big_project_api.controller;
import com.aivle08.big_project_api.model.ResumeRetriever;
import com.aivle08.big_project_api.service.ResumeRetrieverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "ResumeRetriever API", description = "이력서 API")
@RequestMapping("/api/v1/applicant/{applicant-id}/resumeRetriver")
public class ResumeRetrieverController {

    private final ResumeRetrieverService resumeRetrieverService;

    public ResumeRetrieverController(ResumeRetrieverService resumeRetrieverService) {
        this.resumeRetrieverService = resumeRetrieverService;
    }


    @GetMapping
    @Operation(summary = "이력서 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이력서 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<List<ResumeRetriever>> getresumeRetriverList(@PathVariable (name = "applicant-id") Long applicant_id){
        List<ResumeRetriever> resumeRetrievers = resumeRetrieverService.getResumeRetriever(applicant_id);
        return ResponseEntity.ok(resumeRetrievers);
    }

    @PostMapping
    @Operation(summary = "이력서 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이력서 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<ResumeRetriever> createResumeRetriver(@PathVariable (name = "applicant-id") Long applicant_id, @RequestBody ResumeRetriever resumeRetriever)
    {
        ResumeRetriever savedRetriever = resumeRetrieverService.createResumeRetriever(applicant_id, resumeRetriever);
        return ResponseEntity.ok(savedRetriever);
    }
}


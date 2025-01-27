package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.input.RecruitmentInputDTO;
import com.aivle08.big_project_api.dto.output.RecruitmentOutputDTO;
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

    public RecruitmentController(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    @GetMapping
    @Operation(summary = "채용 공고 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공고 리스트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<List<RecruitmentOutputDTO>> getRecruitments() {
        return ResponseEntity.ok()
                .body(recruitmentService.findAllRecruitment());
    }

    @PostMapping
    @Operation(summary = "지원자 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공고 리스트 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<RecruitmentInputDTO> postRecruitment(@RequestBody RecruitmentInputDTO recruitmentInputDTO) {
        recruitmentService.createRecruitment(recruitmentInputDTO);
        return ResponseEntity.ok()
                .body(recruitmentInputDTO);
    }
}

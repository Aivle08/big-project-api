package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.response.QuestionListResponseDTO;
import com.aivle08.big_project_api.service.ApiPipeService;
import com.aivle08.big_project_api.service.ApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai-api")
@Tag(name = "AI API", description = "AI 기반 채용 프로세스 API")
public class AiApiController {
    private final ApiPipeService apiPipeService;
    private final ApiService apiService;

    public AiApiController(ApiPipeService apiPipeService, ApiService apiService) {
        this.apiPipeService = apiPipeService;
        this.apiService = apiService;
    }

    @PostMapping("/{recruitment-id}/resume-pdf")
    @Operation(
            summary = "이력서 PDF 처리",
            description = "채용 공고에 제출된 이력서 PDF를 분석하여 처리합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이력서 처리 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<String> postResumePipeCall(@PathVariable(name = "recruitment-id") Long recruitmentId) {
        apiPipeService.resumePdfPipe(recruitmentId);
        return ResponseEntity.ok("이력서 처리가 완료되었습니다.");
    }

    @PostMapping("/{recruitment-id}/resume-pdf-async")
    @Operation(
            summary = "이력서 PDF 비동기 처리",
            description = "채용 공고에 제출된 이력서 PDF를 비동기적으로 분석하여 처리합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "이력서 처리 시작"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<String> postResumePipeCallAsync(@PathVariable(name = "recruitment-id") Long recruitmentId) {
        apiPipeService.resumePdfPipeAsync(recruitmentId);
        return ResponseEntity.accepted().body("이력서 처리가 시작되었습니다.");
    }

    @PostMapping("/{applicant-id}/question")
    @Operation(
            summary = "면접 질문 생성",
            description = "지원자의 이력서를 기반으로 맞춤형 면접 질문을 생성합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "질문 생성 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<List<QuestionListResponseDTO>> postQuestionCall(@PathVariable(name = "applicant-id") Long applicantId) {
        List<QuestionListResponseDTO> questionListResponseDTOList = apiPipeService.questionPipe(applicantId);
        return ResponseEntity.accepted().body(questionListResponseDTOList);
    }

    @PostMapping("/{recruitment-id}/score")
    @Operation(
            summary = "지원자 평가 점수 계산",
            description = "채용 공고의 지원자들에 대한 평가 점수를 계산합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "점수 계산 시작"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<String> postScoreCall(@PathVariable(name = "recruitment-id") Long recruitmentId) {
        apiPipeService.scorePipe(recruitmentId);
        return ResponseEntity.accepted().body("점수 계산 완료");
    }

    @PostMapping("/{recruitment-id}/score-async")
    @Operation(
            summary = "지원자 평가 점수 비동기 계산",
            description = "채용 공고의 지원자들에 대한 평가 점수를 비동기적으로 계산합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "점수 계산 시작"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<String> postScoreCallAsync(@PathVariable(name = "recruitment-id") Long recruitmentId) {
        apiPipeService.scorePipeAsync2(recruitmentId);
        return ResponseEntity.ok().body("점수 계산이 시작되었습니다.");
    }
}

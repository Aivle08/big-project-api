package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.request.CommentRequestDTO;
import com.aivle08.big_project_api.dto.response.CommentResponseDTO;
import com.aivle08.big_project_api.service.ApiService;
import com.aivle08.big_project_api.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post/{id}/comment")
@Tag(name = "Comment API", description = "댓글 crud API")
public class CommentController {
    private final CommentService commentService;
    private final ApiService apiService;

    public CommentController(CommentService commentService, ApiService apiService) {
        this.commentService = commentService;
        this.apiService = apiService;
    }

    @GetMapping("/{comment-id}")
    @Operation(summary = "댓글 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<CommentResponseDTO> getComment(@PathVariable(name = "comment-id") Long commentId){
        CommentResponseDTO commentResponseDTO = commentService.getComment(commentId);
        return ResponseEntity.ok(commentResponseDTO);
    }


    @PostMapping
    @Operation(summary = "댓글 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable long id, @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO commentResponseDTO = commentService.createComment(id, commentRequestDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }


    @PutMapping({"/{comment-id}"})
    @Operation(summary = "댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long id,@PathVariable(name = "comment-id") Long commentId,
                                                            @RequestBody CommentRequestDTO commentRequestDTO){
        CommentResponseDTO commentResponseDTO = commentService.updateComment(commentId, commentRequestDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }

    @DeleteMapping("/{comment-id}")
    @Operation(summary = "댓글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public String deleteComment(@PathVariable long id, @PathVariable(name = "comment-id") Long commentId) {
        commentService.deleteComment(commentId);
        return null;
    }
}

package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.request.CommentRequestDTO;
import com.aivle08.big_project_api.dto.response.CommentResponseDTO;
import com.aivle08.big_project_api.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post/{id}/comment/{commentId}")
@Tag(name = "Comment API", description = "댓글 crud API")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @Operation(summary = "댓글 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<CommentResponseDTO> postComment(@PathVariable long id, @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO commentResponseDTO = commentService.createComment(id, commentRequestDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }

    @PutMapping
    @Operation(summary = "댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable long id,@RequestBody CommentRequestDTO commentRequestDTO){
        CommentResponseDTO commentResponseDTO = commentService.updateComment(id, commentRequestDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }

    @DeleteMapping
    @Operation(summary = "댓글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public String deleteComment(@PathVariable long id , @PathVariable Long commentId) {
        commentService.deleteComment(id);
        return null;
    }
}

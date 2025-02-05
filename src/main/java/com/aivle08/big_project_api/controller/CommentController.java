package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.request.CommentRequestDTO;
import com.aivle08.big_project_api.service.CommentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post/{id}/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public String postComment(@PathVariable long id, @RequestBody CommentRequestDTO commentRequestDTO) {
        commentService.createComment(id, commentRequestDTO);
        return null;
    }

    @PutMapping
    public String updateComment(@PathVariable long id,@RequestBody CommentRequestDTO commentRequestDTO){
        commentService.updateComment(id, commentRequestDTO);
        return null;
    }

    @DeleteMapping
    public String deleteComment(@PathVariable long id) {
        commentService.deleteComment(id);
        return null;
    }
}

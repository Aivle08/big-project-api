package com.aivle08.big_project_api.dto.response;

import com.aivle08.big_project_api.dto.request.CommentRequestDTO;
import com.aivle08.big_project_api.model.Comment;
import com.aivle08.big_project_api.model.Post;
import com.aivle08.big_project_api.model.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {

    private Long id;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentResponseDTO fromEntity(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .username(comment.getUser().getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}

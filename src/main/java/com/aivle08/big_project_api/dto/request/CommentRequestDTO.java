package com.aivle08.big_project_api.dto.request;

import com.aivle08.big_project_api.model.Comment;
import com.aivle08.big_project_api.model.Users;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequestDTO {
    private Long id;
    private String content;


    public static CommentRequestDTO fromEntity(Comment comment) {
        return CommentRequestDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
    }
}

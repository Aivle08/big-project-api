package com.aivle08.big_project_api.dto.response;

import com.aivle08.big_project_api.model.Post;
import com.aivle08.big_project_api.model.Users;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private Users author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponseDTO fromEntity(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.id = post.getId();
        dto.title = post.getTitle();
        dto.content = post.getContent();
        dto.author = post.getAuthor();
        dto.createdAt = post.getCreatedAt();
        dto.updatedAt = post.getUpdatedAt();
        return dto;
    }
}

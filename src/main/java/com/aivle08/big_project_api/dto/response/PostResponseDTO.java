package com.aivle08.big_project_api.dto.response;

import com.aivle08.big_project_api.model.Comment;
import com.aivle08.big_project_api.model.Post;
import com.aivle08.big_project_api.model.Users;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private Users author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Comment> commentList;

    public static PostResponseDTO fromEntity(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.id = post.getId();
        dto.title = post.getTitle();
        dto.content = post.getContent();
        dto.author = post.getAuthor();
        dto.createdAt = post.getCreatedAt();
        dto.updatedAt = post.getUpdatedAt();
        dto.commentList = post.getComments();
        return dto;
    }
}

package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.request.CommentRequestDTO;
import com.aivle08.big_project_api.dto.response.CommentResponseDTO;
import com.aivle08.big_project_api.model.Comment;
import com.aivle08.big_project_api.model.Post;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.repository.CommentRepository;
import com.aivle08.big_project_api.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UsersService usersService;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UsersService usersService, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.usersService = usersService;
        this.postRepository = postRepository;
    }

    public CommentResponseDTO getComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 댓글을 찾을 수 없습니다. ID: " + id));
        return CommentResponseDTO.fromEntity(comment);
    }

    public CommentResponseDTO createComment(Long id, CommentRequestDTO commentDTO) {

        Users user = usersService.getCurrentUser();

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다. ID: " + id));

        Comment comment = Comment.builder()
                .user(user)
                .content(commentDTO.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .post(post)
                .build();

        Comment savedComment = commentRepository.save(comment);

        return CommentResponseDTO.fromEntity(savedComment);
    }

    public CommentResponseDTO updateComment(Long id, CommentRequestDTO commentDTO) {

        Comment commentById = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 댓글을 찾을 수 없습니다. ID: " + id));

        Comment updateComment = Comment.builder()
                .id(commentById.getId())
                .content(commentDTO.getContent())
                .user(commentById.getUser())
                .updatedAt(LocalDateTime.now())
                .createdAt(commentById.getCreatedAt())
                .post(commentById.getPost())
                .build();

        Comment saveUpdateComment = commentRepository.save(updateComment);

        return CommentResponseDTO.fromEntity(saveUpdateComment);
    }

    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundException("해당 ID의 댓글을 찾을 수 없습니다. ID: " + id);
        }
        commentRepository.deleteById(id);
    }

    public List<CommentResponseDTO> getCommentList(Long id) {
        List<Comment> commentList = commentRepository.findAllByPost_id(id);

        // 🔹 댓글이 없을 경우 예외 처리
        if (commentList.isEmpty()) {
            throw new EntityNotFoundException("해당 게시글(ID: " + id + ")에 대한 댓글이 없습니다.");
        }

        return commentList.stream()
                .map(CommentResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
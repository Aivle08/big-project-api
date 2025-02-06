package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.request.CommentRequestDTO;
import com.aivle08.big_project_api.dto.response.CommentResponseDTO;
import com.aivle08.big_project_api.model.Comment;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.repository.CommentRepository;
import com.aivle08.big_project_api.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        Comment comment = commentRepository.findById(id).orElse(null);
        return CommentResponseDTO.fromEntity(comment);
    }

    public CommentResponseDTO createComment(Long id, CommentRequestDTO commentDTO) {

        Users user = usersService.getCurrentUser();

        Comment comment = Comment.builder()
                .user(user)
                .content(commentDTO.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .post(postRepository.findById(id).get())
                .build();

        Comment savedComment = commentRepository.save(comment);

        return CommentResponseDTO.fromEntity(savedComment);
    }

    public CommentResponseDTO updateComment(Long id, CommentRequestDTO commentDTO) {

        Comment commentById = commentRepository.findById(id).get();

        Comment updateComment = Comment.builder()
                .id(commentById.getId())
                .content(commentDTO.getContent())
                .user(commentById.getUser())
                .updatedAt(LocalDateTime.now())
                .createdAt(commentById.getCreatedAt())
                .post(commentById.getPost())
                .build();

        Comment saveUpdateComment = commentRepository.save(updateComment);

        return  CommentResponseDTO.fromEntity(saveUpdateComment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public List<CommentResponseDTO> getCommentList(Long id) {
        List<Comment> commentList = commentRepository.findAllByPost_id(id);
        List<CommentResponseDTO> commentResponseDTOList = commentList.stream().map(CommentResponseDTO::fromEntity).collect(Collectors.toList());

        return commentResponseDTOList;
    }
}
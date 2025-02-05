package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.request.CommentRequestDTO;
import com.aivle08.big_project_api.model.Comment;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.repository.CommentRepository;
import com.aivle08.big_project_api.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    public Comment createComment(Long id, CommentRequestDTO commentDTO) {

        Users user = usersService.getCurrentUser();

        Comment comment = Comment.builder()
                .user(user)
                .content(commentDTO.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .post(postRepository.findById(id).get())
                .build();

        return commentRepository.save(comment);
    }

    public Comment updateComment(Long id, CommentRequestDTO commentDTO) {

        Comment commentById = commentRepository.findById(id).get();

        Comment updateComment = Comment.builder()
                .id(commentById.getId())
                .content(commentDTO.getContent())
                .user(commentById.getUser())
                .updatedAt(LocalDateTime.now())
                .createdAt(commentById.getCreatedAt())
                .build();

        return  commentRepository.save(updateComment);
    }

    public Comment deleteComment(Long id) {
        Comment commentById = commentRepository.findById(id).get();
        commentRepository.delete(commentById);
        return commentById;
    }

}
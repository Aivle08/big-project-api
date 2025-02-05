package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.request.PostRequestDTO;
import com.aivle08.big_project_api.dto.response.PostResponseDTO;
import com.aivle08.big_project_api.model.Post;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UsersService usersService;
    private final CommentService commentService;

    public PostService(PostRepository postRepository, UsersService usersService, CommentService commentService) {
        this.postRepository = postRepository;
        this.usersService = usersService;
        this.commentService = commentService;
    }

    public PostResponseDTO createPost(PostRequestDTO postRequestDTO) {
        Users author = usersService.getCurrentUser();

        Post createdPost = Post.builder()
                .title(postRequestDTO.getTitle())
                .content(postRequestDTO.getContent())
                .author(author)
                .updatedAt(null)
                .createdAt(LocalDateTime.now())
                .build();

        Post savedPost = postRepository.save(createdPost);

        return PostResponseDTO.fromEntity(savedPost);
    }


    public List<PostResponseDTO> getPostListByCompany() {

        List<Post> posts = postRepository.findByAuthor_Company(usersService.getCurrentUser().getCompany());

        List<PostResponseDTO> postDTOs = posts.stream()
                .map(PostResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return postDTOs;
    }

    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        return PostResponseDTO.fromEntity(post);
    }

    public PostResponseDTO updatePost(Long id, PostRequestDTO requestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        Post updatedPost = Post.builder()
                .id(post.getId())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .author(post.getAuthor())
                .updatedAt(LocalDateTime.now())
                .createdAt(post.getCreatedAt())
                .build();

        Post savedPost = postRepository.save(updatedPost);

        return PostResponseDTO.fromEntity(savedPost);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        if (post.getAuthor().getId().equals(usersService.getCurrentUser().getId())) {
            postRepository.deleteById(id);
        } else throw new RuntimeException("해당 게시글에 접근할 수 없습니다.");
    }
}

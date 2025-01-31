package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.request.PostRequestDTO;
import com.aivle08.big_project_api.dto.response.PostListResponseDTO;
import com.aivle08.big_project_api.dto.response.PostResponseDTO;
import com.aivle08.big_project_api.model.Post;
import com.aivle08.big_project_api.repository.PostRepository;
import com.aivle08.big_project_api.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;

    public PostController(PostRepository postRepository, PostService postService) {
        this.postRepository = postRepository;
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostRequestDTO requestDto) {
        PostResponseDTO createdPost = postService.createPost(requestDto);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping
    public PostListResponseDTO<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll(); // 모든 게시글 조회
        long totalCount = posts.size(); // 총 게시글 수 계산

        List<PostResponseDTO> postDTOs = posts.stream()
                .map(PostResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return new PostListResponseDTO<>(postDTOs, totalCount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        PostResponseDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequestDTO requestDto
    ) {
        PostResponseDTO updatedPost = postService.updatePost(id, requestDto);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}

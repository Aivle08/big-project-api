package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.request.PostRequestDTO;
import com.aivle08.big_project_api.dto.response.PostListResponseDTO;
import com.aivle08.big_project_api.dto.response.PostResponseDTO;
import com.aivle08.big_project_api.model.Post;
import com.aivle08.big_project_api.repository.PostRepository;
import com.aivle08.big_project_api.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "게시글 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostRequestDTO requestDto) {
        PostResponseDTO createdPost = postService.createPost(requestDto);
        return ResponseEntity.ok(createdPost);
    }


    @GetMapping
    @Operation(summary = "전체 게시글 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public PostListResponseDTO<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        long totalCount = posts.size();

        List<PostResponseDTO> postDTOs = posts.stream()
                .map(PostResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return new PostListResponseDTO<>(postDTOs, totalCount);
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시글 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        PostResponseDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }


    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequestDTO requestDto
    ) {
        PostResponseDTO updatedPost = postService.updatePost(id, requestDto);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}

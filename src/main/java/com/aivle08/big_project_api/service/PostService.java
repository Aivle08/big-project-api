package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.request.PostRequestDTO;
import com.aivle08.big_project_api.dto.response.PostResponseDTO;
import com.aivle08.big_project_api.model.Post;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UsersService usersService;

    public PostService(PostRepository postRepository, UsersService usersService) {
        this.postRepository = postRepository;
        this.usersService = usersService;
    }

    public PostResponseDTO createPost(PostRequestDTO postRequestDTO) {
        Users author = usersService.getCurrentUser();

        Post post = new Post(postRequestDTO.getTitle(), postRequestDTO.getContent(), author);
        Post savedPost = postRepository.save(post);

        return PostResponseDTO.fromEntity(savedPost);
    }


    public List<PostResponseDTO> getPostsByCompany() {

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

        if (post.getAuthor().getId().equals(usersService.getCurrentUser().getId())) {
            post.updatePost(requestDto.getTitle(), requestDto.getContent());
            Post updatedPost = postRepository.save(post);

            return PostResponseDTO.fromEntity(updatedPost);
        }
        else throw new RuntimeException("해당 게시글에 접근할 수 없습니다.");
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        if (post.getAuthor().getId().equals(usersService.getCurrentUser().getId())) {
            postRepository.deleteById(id);
        }
        else throw new RuntimeException("해당 게시글에 접근할 수 없습니다.");
    }
}

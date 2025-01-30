package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.request.PostRequestDTO;
import com.aivle08.big_project_api.dto.response.PostResponseDTO;
import com.aivle08.big_project_api.model.Post;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.repository.PostRepository;
import com.aivle08.big_project_api.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UsersRepository usersRepository;

    public PostService(PostRepository postRepository, UsersRepository usersRepository) {
        this.postRepository = postRepository;
        this.usersRepository = usersRepository;
    }

    public PostResponseDTO createPost(PostRequestDTO postRequestDTO) {
        Users author = usersRepository.findByUsername(postRequestDTO.getAuthorId());
//                .orElseThrow(() -> new RuntimeException("작성자(User)가 존재하지 않습니다."));

        Post post = new Post(postRequestDTO.getTitle(), postRequestDTO.getContent(), author);
        Post savedPost = postRepository.save(post);

        return PostResponseDTO.fromEntity(savedPost);
    }

    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        return PostResponseDTO.fromEntity(post);
    }

    public PostResponseDTO updatePost(Long id, PostRequestDTO requestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        post.updatePost(requestDto.getTitle(), requestDto.getContent());
        Post updatedPost = postRepository.save(post);

        return PostResponseDTO.fromEntity(updatedPost);
    }

    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }
        postRepository.deleteById(id);
    }
}

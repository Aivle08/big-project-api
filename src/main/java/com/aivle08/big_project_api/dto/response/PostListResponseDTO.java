package com.aivle08.big_project_api.dto.response;

import lombok.Getter;
import java.util.List;

@Getter
public class PostListResponseDTO<T> {
    private List<T> list;
    private long count; 

    public PostListResponseDTO(List<T> list, long count) {
        this.list = list;
        this.count = count;
    }
}
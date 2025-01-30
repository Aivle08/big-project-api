package com.aivle08.big_project_api.dto.request;

import lombok.Getter;

@Getter
public class PostRequestDTO{
    private String title;
    private String content;
    private String authorId; // authorId을 username으로 바꿈.
}

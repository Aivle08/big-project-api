package com.aivle08.big_project_api.dto.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseDTO<T> {
    private String status;
    private int code;
    private String message;
    private T item;
}

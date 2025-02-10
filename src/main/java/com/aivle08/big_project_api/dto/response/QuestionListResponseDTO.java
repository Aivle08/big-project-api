package com.aivle08.big_project_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionListResponseDTO {
    private String title;
    private List<String> finalQuestion;
    private List<String> chunk;
}

package com.aivle08.big_project_api.dto.api.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponseDTO {
    private QuestionContentDTO question;
    private List<String> chunk;
}
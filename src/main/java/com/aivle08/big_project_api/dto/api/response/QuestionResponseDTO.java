package com.aivle08.big_project_api.dto.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponseDTO {
    @JsonProperty("final_question")
    private List<String> finalQuestion;
}
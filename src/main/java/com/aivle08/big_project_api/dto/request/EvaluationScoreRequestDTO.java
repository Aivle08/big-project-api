package com.aivle08.big_project_api.dto.request;

import com.aivle08.big_project_api.dto.response.EvaluationDetailResponseDTO;
import com.aivle08.big_project_api.model.EvaluationScore;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationScoreRequestDTO {

    private Long evaluationId;

    private Integer score;
    private String summary;

}
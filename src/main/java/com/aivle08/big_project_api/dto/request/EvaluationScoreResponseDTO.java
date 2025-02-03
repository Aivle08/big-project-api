package com.aivle08.big_project_api.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationScoreResponseDTO {
    private Integer score;
    private Long evaluationDetailId;
}
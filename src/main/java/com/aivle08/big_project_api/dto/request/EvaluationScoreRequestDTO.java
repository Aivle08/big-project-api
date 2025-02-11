package com.aivle08.big_project_api.dto.request;

import lombok.*;

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
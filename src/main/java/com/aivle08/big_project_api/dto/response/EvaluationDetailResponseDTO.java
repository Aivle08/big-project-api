package com.aivle08.big_project_api.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationDetailResponseDTO {
    private Integer score;
    private String summary;
    private String title;
}

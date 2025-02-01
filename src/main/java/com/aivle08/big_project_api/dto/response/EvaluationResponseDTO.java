package com.aivle08.big_project_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationResponseDTO {
    private String recruitmentTitle;
    private String applicationName;

    private List<EvaluationDetailResponseDTO> scoreDetails;
}

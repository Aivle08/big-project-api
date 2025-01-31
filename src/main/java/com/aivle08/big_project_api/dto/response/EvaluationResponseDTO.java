package com.aivle08.big_project_api.dto.response;

import com.aivle08.big_project_api.model.EvaluationScore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResponseDTO {
    private String recruitmentTitle;
    private String applicationName;

    private List<EvaluationDetailResponseDTO> scoreDetails;
}

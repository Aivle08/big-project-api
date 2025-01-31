package com.aivle08.big_project_api.dto.request;

import com.aivle08.big_project_api.dto.response.EvaluationResponseDTO;
import com.aivle08.big_project_api.model.Recruitment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentRequestDTO {
    private String title;
    private String job;
    @JsonProperty("evaluationList")
    private List<EvaluationResponseDTO> evaluationResponseDTOList;

    public static RecruitmentRequestDTO fromEntity(Recruitment recruitment) {
        return new RecruitmentRequestDTO(recruitment.getTitle(), recruitment.getJob(),
                recruitment.getEvaluationList().stream().map(EvaluationResponseDTO::fromEntity).toList());
    }
}

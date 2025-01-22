package com.aivle08.big_project_api.dto.input;

import com.aivle08.big_project_api.dto.EvaluationDTO;
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
public class RecruitmentInputDTO {
    private String title;
    private String job;
    @JsonProperty("evaluationList")
    private List<EvaluationDTO> evaluationDTOList;

    public static RecruitmentInputDTO fromEntity(Recruitment recruitment) {
        return new RecruitmentInputDTO(recruitment.getTitle(), recruitment.getJob(),
                recruitment.getEvaluationList().stream().map(EvaluationDTO::fromEntity).toList());
    }
}

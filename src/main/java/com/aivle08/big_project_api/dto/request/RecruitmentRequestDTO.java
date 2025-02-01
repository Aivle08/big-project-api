package com.aivle08.big_project_api.dto.request;

import com.aivle08.big_project_api.model.Recruitment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentRequestDTO {
    private String title;
    private String job;
    @JsonProperty("evaluationList")
    private List<EvaluationRequestDTO> evaluationRequestDTOList;

    public static RecruitmentRequestDTO fromEntity(Recruitment recruitment) {
        return RecruitmentRequestDTO.builder()
                .title(recruitment.getTitle())
                .job(recruitment.getJob())
                .evaluationRequestDTOList(recruitment.getEvaluationList().stream().map(EvaluationRequestDTO::fromEntity).toList())
                .build();
    }
}

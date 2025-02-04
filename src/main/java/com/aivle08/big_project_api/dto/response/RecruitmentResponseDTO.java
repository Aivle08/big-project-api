package com.aivle08.big_project_api.dto.response;

import com.aivle08.big_project_api.dto.request.EvaluationRequestDTO;
import com.aivle08.big_project_api.model.Recruitment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentResponseDTO {

    private Long id;
    private String title;
    private String job;

    @JsonProperty("evaluations")
    private List<EvaluationRequestDTO> evaluationRequestDTOList;

    public static RecruitmentResponseDTO fromEntity(Recruitment recruitment) {
        return RecruitmentResponseDTO.builder()
                .id(recruitment.getId())
                .title(recruitment.getTitle())
                .job(recruitment.getJob())
                .evaluationRequestDTOList(recruitment.getEvaluationList().stream().map(EvaluationRequestDTO::fromEntity).toList())
                .build();
    }
}

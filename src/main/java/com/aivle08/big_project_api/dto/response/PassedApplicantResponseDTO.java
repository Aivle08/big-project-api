package com.aivle08.big_project_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassedApplicantResponseDTO {
    private String recruitmentTitle;
    private List<EvaluationResponseDTO> passList;
}
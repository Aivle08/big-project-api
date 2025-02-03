package com.aivle08.big_project_api.dto.request;

import com.aivle08.big_project_api.model.Evaluation;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationRequestDTO {
    private String item;
    private String detail;

    public static Evaluation toEntity(EvaluationRequestDTO evaluationRequestDTO) {
        return Evaluation.builder()
                .item(evaluationRequestDTO.getItem())
                .detail(evaluationRequestDTO.getDetail())
                .build();
    }

    public static EvaluationRequestDTO fromEntity(Evaluation evaluation) {
        return EvaluationRequestDTO.builder()
                .item(evaluation.getItem())
                .detail(evaluation.getDetail())
                .build();
    }

}

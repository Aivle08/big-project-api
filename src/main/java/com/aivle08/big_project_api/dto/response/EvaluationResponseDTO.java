package com.aivle08.big_project_api.dto.response;

import com.aivle08.big_project_api.model.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResponseDTO {
    private String item;
    private String detail;

    public static Evaluation toEntity(EvaluationResponseDTO evaluationResponseDTO) {
        return new Evaluation(null, evaluationResponseDTO.getItem(), evaluationResponseDTO.getDetail(), null, null);
    }

    public static EvaluationResponseDTO fromEntity(Evaluation evaluation) {
        return new EvaluationResponseDTO(evaluation.getItem(), evaluation.getDetail());
    }
}

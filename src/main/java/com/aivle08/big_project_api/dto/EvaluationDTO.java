package com.aivle08.big_project_api.dto;

import com.aivle08.big_project_api.model.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDTO {
    private String item;
    private String detail;

    public static Evaluation toEntity(EvaluationDTO evaluationDTO) {
        return new Evaluation(null, evaluationDTO.getItem(), evaluationDTO.getDetail(), null, null);
    }

    public static EvaluationDTO fromEntity(Evaluation evaluation) {
        return new EvaluationDTO(evaluation.getItem(), evaluation.getDetail());
    }
}

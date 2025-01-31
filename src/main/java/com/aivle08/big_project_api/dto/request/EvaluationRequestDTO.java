package com.aivle08.big_project_api.dto.request;

import com.aivle08.big_project_api.model.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationRequestDTO {
    private String item;
    private String detail;

    public static Evaluation toEntity(EvaluationRequestDTO evaluationRequestDTO) {
        return new Evaluation(null, evaluationRequestDTO.getItem(), evaluationRequestDTO.getDetail(), null, null);
    }

    public static EvaluationRequestDTO fromEntity(Evaluation evaluation) {
        return new EvaluationRequestDTO(evaluation.getItem(), evaluation.getDetail());
    }

}

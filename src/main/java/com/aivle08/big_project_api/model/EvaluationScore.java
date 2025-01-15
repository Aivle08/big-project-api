package com.aivle08.big_project_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long score;

    @OneToOne
    @JoinColumn(name = "evaluation_detail_id")
    private EvaluationDetail evaluationDetail;
}

package com.aivle08.big_project_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EvaluationScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer score;

    @OneToOne
    @JoinColumn(name = "evaluation_detail_id")
    private EvaluationDetail evaluationDetail;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;
}

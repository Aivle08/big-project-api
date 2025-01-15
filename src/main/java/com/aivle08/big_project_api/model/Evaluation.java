package com.aivle08.big_project_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String item;
    private String detail;

    @OneToMany
    @JsonIgnore
    @JoinColumn(name = "evaluation_id")
    private List<EvaluationScore> evaluationScoreList;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;
}

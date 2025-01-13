package com.aivle08.big_project_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String contact;
    private String fileName;
    private Boolean resumeResult;
    private String resumeSummary;

    @OneToMany
    @JoinColumn(name = "applicant_id")
    private List<EvaluationScore> evaluationScoreList;
}

package com.aivle08.big_project_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String contact;
    private String fileName;
    private Boolean resumeResult;
    @Column(columnDefinition = "TEXT")
    private String resumeSummary;

    @OneToMany
    @JoinColumn(name = "applicant_id")
    private List<EvaluationScore> evaluationScoreList;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;
}

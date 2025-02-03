package com.aivle08.big_project_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Recruitment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String title;
    private String job;

    @OneToMany(mappedBy = "recruitment")
    private List<Evaluation> evaluationList;

    @OneToMany(mappedBy = "recruitment")
    private List<Applicant> applicantList;

    @ManyToOne
    @JsonIgnore
    private Department department;
}

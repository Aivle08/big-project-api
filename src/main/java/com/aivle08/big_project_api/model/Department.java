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
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JsonIgnore
    private Company company;

    @OneToMany
    @JoinColumn(name = "department_id")
    private List<Evaluation> evaluationList;

    @OneToMany
    @JoinColumn(name = "department_id")
    private List<Applicant> applicantList;

}

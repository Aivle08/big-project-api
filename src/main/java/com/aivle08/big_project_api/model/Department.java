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
@Table(
        name = "department",
        uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "name"})
)
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "department")
    private List<Users> usersList;

    @OneToMany
    @JoinColumn(name = "department_id")
    private List<Evaluation> evaluationList;

    @OneToMany
    @JoinColumn(name = "department_id")
    private List<Applicant> applicantList;

}

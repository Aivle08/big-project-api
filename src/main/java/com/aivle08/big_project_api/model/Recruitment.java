package com.aivle08.big_project_api.model;

import com.aivle08.big_project_api.constants.ProcessingStatus;
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

    @Enumerated(EnumType.STRING)
    private ProcessingStatus processingStatus = ProcessingStatus.NOT_STARTED;

    @Builder
    public Recruitment(String title) {
        this.title = title;
        this.processingStatus = ProcessingStatus.NOT_STARTED;
    }

    public void updateProcessingStatus(ProcessingStatus newStatus) {
        this.processingStatus = newStatus;
    }
}

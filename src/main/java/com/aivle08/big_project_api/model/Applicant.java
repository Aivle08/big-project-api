package com.aivle08.big_project_api.model;

import com.aivle08.big_project_api.constants.ProcessingStatus;
import com.aivle08.big_project_api.dto.api.response.ExtractionResponseDTO;
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

    @Enumerated(EnumType.STRING)
    private ProcessingStatus processingStatus;

    @Builder
    public Applicant(String name, String fileName) {
        this.name = name;
        this.fileName = fileName;
        this.processingStatus = ProcessingStatus.NOT_STARTED;
    }

    // 상태 변경 메서드
    public void updateProcessingStatus(ProcessingStatus status) {
        this.processingStatus = status;
    }

    // API 응답을 바탕으로 지원자 정보 업데이트
    public void updateWithApiResponse(ExtractionResponseDTO dto, ProcessingStatus status) {
        this.name = dto.getName();
        this.resumeSummary = dto.getElseSummary();
        this.contact = dto.getPhone();
        this.email = dto.getEmail();
        this.resumeResult = true;
        this.processingStatus = status;
    }
}

package com.aivle08.big_project_api.dto.request;

import com.aivle08.big_project_api.model.Applicant;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantRequestDTO {

    private String name;
    private String email;
    private String contact;
    private String fileName;
    private Boolean resumeResult;
    private String resumeSummary;

    public static ApplicantRequestDTO fromEntity(Applicant applicant) {
        return ApplicantRequestDTO.builder()
                .name(applicant.getName())
                .email(applicant.getEmail())
                .contact(applicant.getContact())
                .fileName(applicant.getFileName())
                .resumeResult(false)
                .resumeSummary(applicant.getResumeSummary())
                .build();
    }
}

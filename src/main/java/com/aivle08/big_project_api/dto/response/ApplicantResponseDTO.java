package com.aivle08.big_project_api.dto.response;

import com.aivle08.big_project_api.model.Applicant;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String contact;
    private String fileName;
    private Boolean resumeResult;
    private String resumeSummary;

    public static com.aivle08.big_project_api.dto.response.ApplicantResponseDTO fromEntity(Applicant applicant) {
        return com.aivle08.big_project_api.dto.response.ApplicantResponseDTO.builder()
                .id(applicant.getId())
                .name(applicant.getName())
                .email(applicant.getEmail())
                .contact(applicant.getContact())
                .fileName(applicant.getFileName())
                .resumeResult(false)
                .resumeSummary(applicant.getResumeSummary())
                .build();
    }
}
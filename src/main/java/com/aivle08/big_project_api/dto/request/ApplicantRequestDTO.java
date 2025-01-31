package com.aivle08.big_project_api.dto.request;

import com.aivle08.big_project_api.model.Applicant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantRequestDTO {

    private String name;
    private String email;
    private String contact;
    private String fileName;
    private Boolean resumeResult;
    private String resumeSummary;

    public static ApplicantRequestDTO fromEntity(Applicant applicant) {
        return new ApplicantRequestDTO(applicant.getName(), applicant.getEmail(),
                applicant.getContact(), applicant.getFileName(), false,
                applicant.getResumeSummary());
    }
}

package com.aivle08.big_project_api.dto.input;


import com.aivle08.big_project_api.model.Applicant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantInputDTO {

    private String name;
    private String email;
    private String contact;
    private String fileName;
    private Boolean resumeResult;
    private String resumeSummary;

    private Long recruitmentId;

    public static ApplicantInputDTO fromEntity(Applicant applicant) {
        return new ApplicantInputDTO(applicant.getName(), applicant.getEmail(),
                applicant.getContact(), applicant.getFileName(), false,
                applicant.getResumeSummary(), applicant.getRecruitment().getId());
    }
}

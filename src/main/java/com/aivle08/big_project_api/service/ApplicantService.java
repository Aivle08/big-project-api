package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.input.ApplicantInputDTO;
import com.aivle08.big_project_api.dto.input.RecruitmentInputDTO;
import com.aivle08.big_project_api.dto.output.RecruitmentOutputDTO;
import com.aivle08.big_project_api.model.Applicant;
import com.aivle08.big_project_api.model.Recruitment;
import com.aivle08.big_project_api.repository.ApplicantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicantService {
    private final ApplicantRepository applicantRepository;

    public ApplicantService(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    public List<Applicant> getApplicants() {
        return applicantRepository.findAll();
    }

    public List<ApplicantInputDTO> getApplicantsByRecruitmentId(Long recruitmentId) {
        List<Applicant> applicant = applicantRepository.findByRecruitmentId(recruitmentId);

        List<ApplicantInputDTO> applicantInputDTOList = applicant.stream().map(ApplicantInputDTO::fromEntity).toList();
        return applicantInputDTOList;
    }

//    public ApplicantInputDTO createApplicant(List<ApplicantInputDTO> applicantInputDTOList) {
//        ApplicantInputDTO applicant = new ApplicantInputDTO(applicantInputDTO.getName(), applicantInputDTO.getEmail(), applicantInputDTO.getContact(), applicantInputDTO.getFileName(), false, applicantInputDTO.getResumeSummary(), null);
//
//
//        return applicantRepository.save(applicant);
//    }
}

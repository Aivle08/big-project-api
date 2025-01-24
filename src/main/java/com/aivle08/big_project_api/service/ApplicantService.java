package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.input.ApplicantInputDTO;
import com.aivle08.big_project_api.model.Applicant;
import com.aivle08.big_project_api.model.Recruitment;
import com.aivle08.big_project_api.repository.ApplicantRepository;
import com.aivle08.big_project_api.repository.RecruitmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final RecruitmentRepository recruitmentRepository;

    public ApplicantService(ApplicantRepository applicantRepository, RecruitmentRepository recruitmentRepository) {
        this.applicantRepository = applicantRepository;
        this.recruitmentRepository = recruitmentRepository;
    }

    public List<Applicant> getApplicants() {
        return applicantRepository.findAll();
    }

    public List<ApplicantInputDTO> getApplicantsByRecruitmentId(Long recruitmentId) {
        List<Applicant> applicant = applicantRepository.findByRecruitmentId(recruitmentId);

        List<ApplicantInputDTO> applicantInputDTOList = applicant.stream().map(ApplicantInputDTO::fromEntity).toList();
        return applicantInputDTOList;
    }

    @Transactional
    public ApplicantInputDTO applicantCreate(ApplicantInputDTO applicantInputDTO, Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElse(null);

        Applicant applicant = new Applicant(
                null,
                applicantInputDTO.getName(),
                applicantInputDTO.getEmail(),
                applicantInputDTO.getContact(),
                applicantInputDTO.getFileName(),
                false,
                applicantInputDTO.getResumeSummary(),
                null,
                recruitment
        );

        Applicant savedApplicant = applicantRepository.save(applicant);

        return ApplicantInputDTO.fromEntity(savedApplicant);
    }
}

package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.request.ApplicantRequestDTO;
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

    public List<ApplicantRequestDTO> getApplicantsByRecruitmentId(Long recruitmentId) {
        List<Applicant> applicant = applicantRepository.findByRecruitmentId(recruitmentId);

        List<ApplicantRequestDTO> applicantRequestDTOList = applicant.stream().map(ApplicantRequestDTO::fromEntity).toList();
        return applicantRequestDTOList;
    }

    public List<Long> getApplicantIDsByRecruitmentId(Long recruitmentId) {
        List<Applicant> applicants = applicantRepository.findByRecruitmentId(recruitmentId);

        List<Long> applicantIds = applicants.stream()
                .map(Applicant::getId)
                .toList();

        return applicantIds;
    }

    @Transactional
    public ApplicantRequestDTO applicantCreate(ApplicantRequestDTO applicantRequestDTO, Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElse(null);

        Applicant applicant = Applicant.builder()
                .name(applicantRequestDTO.getName())
                .email(applicantRequestDTO.getEmail())
                .contact(applicantRequestDTO.getContact())
                .fileName(applicantRequestDTO.getFileName())
                .resumeResult(false)
                .resumeSummary(applicantRequestDTO.getResumeSummary())
                .recruitment(recruitment)
                .build();

        Applicant savedApplicant = applicantRepository.save(applicant);

        return ApplicantRequestDTO.fromEntity(savedApplicant);
    }
}

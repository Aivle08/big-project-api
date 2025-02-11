package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.request.ApplicantRequestDTO;
import com.aivle08.big_project_api.dto.response.ApplicantResponseDTO;
import com.aivle08.big_project_api.model.Applicant;
import com.aivle08.big_project_api.model.Recruitment;
import com.aivle08.big_project_api.repository.ApplicantRepository;
import com.aivle08.big_project_api.repository.RecruitmentRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public List<ApplicantResponseDTO> getApplicantListByRecruitmentId(Long recruitmentId) {
        List<Applicant> applicants = applicantRepository.findByRecruitmentId(recruitmentId);

        if (applicants.isEmpty()) {
            throw new EntityNotFoundException("해당 채용 공고(ID: " + recruitmentId + ")에 지원한 지원자가 없습니다.");
        }

        List<ApplicantResponseDTO> applicantResponseDTOList = applicants.stream().map(ApplicantResponseDTO::fromEntity).toList();

        return applicantResponseDTOList;
    }

    public List<Long> getApplicantIdListByRecruitmentId(Long recruitmentId) {
        List<Applicant> applicants = applicantRepository.findByRecruitmentId(recruitmentId);

        if (applicants.isEmpty()) {
            throw new EntityNotFoundException("해당 채용 공고(ID: " + recruitmentId + ")에 지원한 지원자가 없습니다.");
        }

        List<Long> applicantIdList = applicants.stream()
                .map(Applicant::getId)
                .toList();

        return applicantIdList;
    }

    @Transactional
    public ApplicantRequestDTO createApplicant(ApplicantRequestDTO applicantRequestDTO, Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 채용 정보를 찾을 수 없습니다. ID: " + recruitmentId));

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

    public ApplicantResponseDTO getApplicantById(Long applicantId) {
        Applicant applicant = applicantRepository.findById(applicantId).get();
        return ApplicantResponseDTO.fromEntity(applicant);
    }
}

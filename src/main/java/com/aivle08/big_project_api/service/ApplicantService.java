package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.model.Applicant;
import com.aivle08.big_project_api.repository.ApplicantRepository;
import org.springframework.stereotype.Service;

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
}

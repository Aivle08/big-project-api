package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.model.Applicant;
import com.aivle08.big_project_api.service.ApplicantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applicant")
public class ApplicantController {
    private final ApplicantService applicantService;

    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @GetMapping
    public List<Applicant> getApplicants() {
        return applicantService.getApplicants();
    }
}

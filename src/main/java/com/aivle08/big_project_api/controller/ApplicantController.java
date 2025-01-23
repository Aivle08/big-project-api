package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.input.ApplicantInputDTO;
import com.aivle08.big_project_api.model.Applicant;
import com.aivle08.big_project_api.service.ApplicantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class ApplicantController {
    private final ApplicantService applicantService;

    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @GetMapping
    public List<Applicant> getApplicants() {
        return applicantService.getApplicants();
    }

    @GetMapping("/recruitment/{recruitment_id}/applicant")
    public ResponseEntity<?> getApplicantsByRecruitmentId(@PathVariable Long recruitmentId) {

        List<ApplicantInputDTO> applicantsInputDTO = applicantService.getApplicantsByRecruitmentId(recruitmentId);

        return ResponseEntity.ok().body(applicantsInputDTO);
    }
}

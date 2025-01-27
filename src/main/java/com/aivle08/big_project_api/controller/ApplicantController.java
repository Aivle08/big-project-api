package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.input.ApplicantInputDTO;
import com.aivle08.big_project_api.service.ApplicantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruitment/{id}")
public class ApplicantController {
    private final ApplicantService applicantService;

    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @GetMapping("/applicant")
    public ResponseEntity<?> getApplicantsByRecruitmentId(@PathVariable Long id) {

        List<ApplicantInputDTO> applicantsInputDTO = applicantService.getApplicantsByRecruitmentId(id);

        return ResponseEntity.ok().body(applicantsInputDTO);
    }

    @PostMapping
    public ResponseEntity<?> postRecruitment(@RequestBody ApplicantInputDTO applicantInputDTO, @PathVariable Long id) {
        applicantService.applicantCreate(applicantInputDTO, id);
        return ResponseEntity.ok()
                .body(applicantInputDTO);
    }
}

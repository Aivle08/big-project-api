package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.model.Recruitment;
import com.aivle08.big_project_api.service.RecruitmentService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruitment")
@CrossOrigin
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    public RecruitmentController(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    @GetMapping
    public List<Recruitment> getRecruitments() {
        return recruitmentService.getAllRecruitment();
    }
}

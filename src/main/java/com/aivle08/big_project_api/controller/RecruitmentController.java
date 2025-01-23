package com.aivle08.big_project_api.controller;

import com.aivle08.big_project_api.dto.input.RecruitmentInputDTO;
import com.aivle08.big_project_api.dto.output.RecruitmentOutputDTO;
import com.aivle08.big_project_api.service.RecruitmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<RecruitmentOutputDTO>>  getRecruitments() {
        return ResponseEntity.ok()
                .body(recruitmentService.findAllRecruitment());
    }

    @PostMapping
    public ResponseEntity<RecruitmentInputDTO> postRecruitment(@RequestBody RecruitmentInputDTO recruitmentInputDTO) {
        recruitmentService.createRecruitment(recruitmentInputDTO);
        return ResponseEntity.ok()
                .body(recruitmentInputDTO);
    }
}

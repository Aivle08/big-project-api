package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.model.Recruitment;
import com.aivle08.big_project_api.repository.RecruitmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;

    public RecruitmentService(RecruitmentRepository recruitmentRepository) {
        this.recruitmentRepository = recruitmentRepository;
    }

    public List<Recruitment> getAllRecruitment() {
        return recruitmentRepository.findAll();
    }
}

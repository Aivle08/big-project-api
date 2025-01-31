package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.request.RecruitmentRequestDTO;
import com.aivle08.big_project_api.dto.request.EvaluationRequestDTO;
import com.aivle08.big_project_api.dto.response.RecruitmentResponseDTO;
import com.aivle08.big_project_api.model.Department;
import com.aivle08.big_project_api.model.Evaluation;
import com.aivle08.big_project_api.model.Recruitment;
import com.aivle08.big_project_api.repository.EvaluationRepository;
import com.aivle08.big_project_api.repository.RecruitmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;
    private final EvaluationRepository evaluationRepository;
    private final UsersService usersService;

    public RecruitmentService(RecruitmentRepository recruitmentRepository, EvaluationRepository evaluationRepository, UsersService usersService) {
        this.recruitmentRepository = recruitmentRepository;
        this.evaluationRepository = evaluationRepository;
        this.usersService = usersService;
    }

    public List<RecruitmentResponseDTO> findAllRecruitment() {
        return recruitmentRepository.findAllByDepartment(usersService.getCurrentUser().getDepartment())
                .stream().map(RecruitmentResponseDTO::fromEntity).toList();
    }

    @Transactional
    public RecruitmentRequestDTO createRecruitment(RecruitmentRequestDTO recruitmentRequestDTO) {
        List<Evaluation> evaluations = recruitmentRequestDTO.getEvaluationRequestDTOList()
                .stream().map(EvaluationRequestDTO::toEntity).toList();

        Department department = usersService.getCurrentUser().getDepartment();

        Recruitment recruitment = new Recruitment(null, LocalDateTime.now(), LocalDateTime.now(), recruitmentRequestDTO.getTitle(), recruitmentRequestDTO.getJob(), evaluations, null, department);
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);

        List<Evaluation> evaluationList = evaluations
                .stream()
                .map((evaluation) -> new Evaluation(null, evaluation.getItem(), evaluation.getDetail(), null, savedRecruitment)).toList();
        List<Evaluation> savedEvaluations = evaluationRepository.saveAll(evaluationList);

        RecruitmentRequestDTO savedRecruitmentRequestDTO = RecruitmentRequestDTO.fromEntity(savedRecruitment);
        return savedRecruitmentRequestDTO;
    }
}

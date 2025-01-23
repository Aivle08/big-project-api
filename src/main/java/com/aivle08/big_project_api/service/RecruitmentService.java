package com.aivle08.big_project_api.service;

import com.aivle08.big_project_api.dto.EvaluationDTO;
import com.aivle08.big_project_api.dto.input.RecruitmentInputDTO;
import com.aivle08.big_project_api.dto.output.RecruitmentOutputDTO;
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

    public List<RecruitmentOutputDTO> findAllRecruitment() {
        return recruitmentRepository.findAllByDepartment(usersService.getCurrentUser().getDepartment())
                .stream().map(RecruitmentOutputDTO::fromEntity).toList();
    }

    @Transactional
    public RecruitmentInputDTO createRecruitment(RecruitmentInputDTO recruitmentInputDTO) {
        List<Evaluation> evaluations = recruitmentInputDTO.getEvaluationDTOList()
                .stream().map(EvaluationDTO::toEntity).toList();

        Department department = usersService.getCurrentUser().getDepartment();

        Recruitment recruitment = new Recruitment(null, LocalDateTime.now(), LocalDateTime.now(), recruitmentInputDTO.getTitle(), recruitmentInputDTO.getJob(), evaluations, null, department);
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);

        List<Evaluation> evaluationList = evaluations
                .stream()
                .map((evaluation) -> new Evaluation(null, evaluation.getItem(), evaluation.getDetail(), null, savedRecruitment)).toList();
        List<Evaluation> savedEvaluations = evaluationRepository.saveAll(evaluationList);

        RecruitmentInputDTO savedRecruitmentInputDTO = RecruitmentInputDTO.fromEntity(savedRecruitment);
        return savedRecruitmentInputDTO;
    }
}

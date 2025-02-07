package com.aivle08.big_project_api;

import com.aivle08.big_project_api.model.Evaluation;
import com.aivle08.big_project_api.model.Recruitment;
import com.aivle08.big_project_api.model.Users;
import com.aivle08.big_project_api.repository.ApplicantRepository;
import com.aivle08.big_project_api.repository.EvaluationRepository;
import com.aivle08.big_project_api.repository.RecruitmentRepository;
import com.aivle08.big_project_api.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class UsersServiceTests {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;


    @Test
    void updateUser() {
        Users users = Users.builder()
                .username("chan")
                .email("string")
                .password(passwordEncoder.encode("chan"))
                .verifiedEmail(true)
                .build();
        usersRepository.save(users);

        List<Evaluation> evaluations = List.of(
                Evaluation.builder()
                        .item("고객 가치")
                        .detail("Understanding customer needs and solving problems actively.")
                        .build(),
                Evaluation.builder()
                        .item("실력")
                        .detail("Possess expertise and skills in the related field (e.g., 5G, AI, IoT).")
                        .build(),
                Evaluation.builder()
                        .item("역량")
                        .detail("Focus on real results rather than glamorous credentials.")
                        .build(),
                Evaluation.builder()
                        .item("팀워크")
                        .detail("Experience in teamwork and collaboration with diverse people.")
                        .build(),
                Evaluation.builder()
                        .item("리더쉽")
                        .detail("Experience leading a team to achieve objectives.")
                        .build()
        );

        Recruitment recruitment = Recruitment.builder()
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .title("title")
                .job("IT영업")
                .department(null)
                .build();

        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);

        List<Evaluation> evaluationList = evaluations.stream()
                .map(evaluation -> Evaluation.builder()
                        .item(evaluation.getItem())
                        .detail(evaluation.getDetail())
                        .recruitment(savedRecruitment)
                        .build())
                .toList();
        List<Evaluation> savedEvaluations = evaluationRepository.saveAll(evaluationList);
        
    }
}

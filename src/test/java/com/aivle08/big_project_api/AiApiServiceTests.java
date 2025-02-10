package com.aivle08.big_project_api;

import com.aivle08.big_project_api.dto.api.request.QuestionRequestDTO;
import com.aivle08.big_project_api.dto.api.response.ApiResponseDTO;
import com.aivle08.big_project_api.dto.api.response.QuestionResponseDTO;
import com.aivle08.big_project_api.service.ApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AiApiServiceTests {
    @Autowired
    private ApiService apiService;

    @Test
    public void test() {
        QuestionRequestDTO questionRequestDTO = QuestionRequestDTO.builder()
                        .job("It영업")
                                .applicantId(101L)
                                        .companyId(1L)
                                                .build();

        ApiResponseDTO<QuestionResponseDTO> dto = apiService.callQuestionApi(questionRequestDTO, "tech");


    }
}

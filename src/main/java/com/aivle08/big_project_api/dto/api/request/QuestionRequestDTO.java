package com.aivle08.big_project_api.dto.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRequestDTO
{
    private String job;
    @JsonProperty("company_id")
    private Long companyId;
    @JsonProperty("applicant_id")
    private Long applicantId;
    private String evaluation;
}
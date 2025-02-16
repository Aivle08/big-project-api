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
public class ScoreRequestDTO {
    private String job;
    @JsonProperty("eval_item")
    private String evalItem;
    @JsonProperty("eval_item_content")
    private String evalItemContent;
    @JsonProperty("applicant_id")
    private Long applicantId;
}
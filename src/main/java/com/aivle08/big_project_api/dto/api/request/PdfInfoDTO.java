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
public class PdfInfoDTO {

    @JsonProperty("pdf_name")
    private String pdfName;

    @JsonProperty("applicant_id")
    private Long applicantId;
}
